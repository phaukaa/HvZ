package no.noroff.hvz.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import no.noroff.hvz.dto.mission.MissionDTO;
import no.noroff.hvz.dto.mission.MissionDTOReg;
import no.noroff.hvz.exceptions.AppUserNotFoundException;
import no.noroff.hvz.exceptions.MissingPermissionsException;
import no.noroff.hvz.mapper.Mapper;
import no.noroff.hvz.models.Mission;
import no.noroff.hvz.models.Player;
import no.noroff.hvz.security.SecurityUtils;
import no.noroff.hvz.services.MissionService;
import no.noroff.hvz.services.PlayerService;
import no.noroff.hvz.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/game/{gameID}/mission")
@CrossOrigin(origins = "*")
public class MissionController {

    @Autowired
    private MissionService missionService;
    @Autowired
    private Mapper mapper;
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private PlayerService playerService;


    @Operation(tags = "Mission", summary = "Get all Missions", description = "Method for getting all missions in a game. Admin get all missions, " +
            "players get their factions missions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Missions found.",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = MissionDTO.class))) }),
            @ApiResponse(responseCode = "401", description = "User not logged in.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "404", description = "No game with that ID.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
    })
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MissionDTO>> getAllMissions(
            @PathVariable Long gameID,
            @AuthenticationPrincipal Jwt principal) throws AppUserNotFoundException {
        HttpStatus status;
        List<MissionDTO> missionDTOs;
        List<Mission> missions;
        if(SecurityUtils.isAdmin(principal.getTokenValue())) {
            missions = missionService.getAllMissions(gameID);
        }
        else {
            Player player = playerService.getPlayerByGameAndUser(gameID, appUserService.getSpecificUser(principal.getClaimAsString("sub")));
            missions = missionService.getAllMissionsFaction(gameID, player.isHuman());
        }
        missionDTOs = missions.stream().map(mapper::toMissionDTO).collect(Collectors.toList());
        status = HttpStatus.OK;
        return new ResponseEntity<>(missionDTOs, status);
    }


    @Operation(tags = "Mission", summary = "Get a specific mission", description = "Method for getting a specific mission in a game. Players can only get their factions missions.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mission found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MissionDTO.class)) }),
            @ApiResponse(responseCode = "401", description = "User not logged in.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "404", description = "No game with that gameID, or no mission with that missionID.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
    })
    @GetMapping("/{missionID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MissionDTO> getSpecificMission(@PathVariable Long gameID, @PathVariable Long missionID, @AuthenticationPrincipal Jwt principal) throws AppUserNotFoundException, MissingPermissionsException {
        Mission mission = missionService.getSpecificMission(gameID,missionID);
        if(!SecurityUtils.isAdmin(principal.getTokenValue())) {
            Player player = playerService.getPlayerByGameAndUser(gameID, appUserService.getSpecificUser(principal.getClaimAsString("sub")));
            if(mission.isHuman() != player.isHuman()) {
                throw new MissingPermissionsException("User does not have the right permissions for this operation");
            }
        }
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(mapper.toMissionDTO(mission),status);
    }


    @Operation(tags = "Mission", summary = "Create a mission", description = "Method for creating a new mission in a game. Admin only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Mission created.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MissionDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Mission registration json incorrect.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "401", description = "User not logged in.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "403", description = "User is not an admin.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "404", description = "No game with that gameID.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = MissionDTOReg.class)))
    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    public ResponseEntity<MissionDTO> createNewMission(@PathVariable Long gameID, @RequestBody MissionDTOReg missionDTO) {
        Mission mission = mapper.toMission(missionDTO, gameID);
        mission = missionService.createNewMission(gameID, mission);
        MissionDTO addedMissionDTO = mapper.toMissionDTO(mission);
        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(addedMissionDTO, status);
    }


    @Operation(tags = "Mission", summary = "Update a mission", description = "Method for updating a mission in a game. Admin only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mission updated.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MissionDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Mission registration json incorrect.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "401", description = "User not logged in.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "403", description = "User is not an admin.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "404", description = "No game with that gameID.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = MissionDTOReg.class)))
    @PutMapping("/{missionID}")
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    public ResponseEntity<MissionDTO> updateMission(@PathVariable Long gameID, @PathVariable Long missionID, @RequestBody MissionDTOReg missionDTO) {
        Mission mission = mapper.toMissionUpdate(missionDTO, gameID, missionID);
        Mission updatedMission = missionService.updateMission(gameID, missionID, mission);
        MissionDTO updatedMissionDTO = mapper.toMissionDTO(updatedMission);
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(updatedMissionDTO, status);
    }


    @Operation(tags = "Mission", summary = "Delete a mission", description = "Method for deleting a mission in a game. Admin only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mission deleted.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MissionDTO.class)) }),
            @ApiResponse(responseCode = "401", description = "User not logged in.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "403", description = "User is not an admin.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "404", description = "No game with that gameID or mission with that missionID.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
    })
    @DeleteMapping("/{missionID}")
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    public ResponseEntity<MissionDTO> deleteMission(@PathVariable Long gameID, @PathVariable Long missionID) {
        Mission deletedMission = missionService.deleteMission(gameID, missionID);
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(mapper.toMissionDTO(deletedMission), status);
    }
}
