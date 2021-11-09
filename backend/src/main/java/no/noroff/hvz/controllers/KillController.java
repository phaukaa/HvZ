package no.noroff.hvz.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import no.noroff.hvz.dto.kill.KillDTO;
import no.noroff.hvz.dto.kill.KillDTOReg;
import no.noroff.hvz.exceptions.AppUserNotFoundException;
import no.noroff.hvz.exceptions.InvalidBiteCodeException;
import no.noroff.hvz.exceptions.MissingPermissionsException;
import no.noroff.hvz.mapper.Mapper;
import no.noroff.hvz.models.Kill;
import no.noroff.hvz.security.SecurityUtils;
import no.noroff.hvz.services.KillerService;
import no.noroff.hvz.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/game/{gameID}/kill")
@CrossOrigin(origins = "*")
public class KillController {

    @Autowired
    private KillerService killerService;
    @Autowired
    private AppUserService appUserService;
    @Autowired
    Mapper mapper;


    @Operation(tags = "Kill", summary = "Get all kills", description = "Method for getting all kills in a game.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kills found.",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = KillDTO.class))) }),
            @ApiResponse(responseCode = "401", description = "User not logged in.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "404", description = "No game with that ID.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
    })
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<KillDTO>> getAllKills(@PathVariable Long gameID, @RequestParam(required = false) Long killerID) {
        List<Kill> kills;
        if (killerID != null) kills = killerService.getAllKills(gameID, killerID);
        else kills = killerService.getAllKills(gameID);
        HttpStatus status = HttpStatus.OK;
        List<KillDTO> killDTOs = kills.stream().map(mapper::toKillDTO).collect(Collectors.toList());
        return new ResponseEntity<>(killDTOs, status);
    }


    @Operation(tags = "Kill", summary = "Get a specific kill", description = "Method for getting a specific kill in a game.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kill found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = KillDTO.class)) }),
            @ApiResponse(responseCode = "401", description = "User not logged in.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "404", description = "No game with that gameID, or no kill with that killID.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
    })
    @GetMapping("/{killID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<KillDTO> getSpecificKill(@PathVariable Long gameID, @PathVariable Long killID) {
        Kill kill = killerService.getSpecificKill(gameID, killID);
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(mapper.toKillDTO(kill), status);
    }


    @Operation(tags = "Kill", summary = "Create a kill", description = "Method for creating a new kill in a game.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Kill created.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = KillDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Kill registration json incorrect. Check that the BiteCode is correct.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "401", description = "User not logged in.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "404", description = "No game with that gameID.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = KillDTOReg.class)))
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<KillDTO> createNewKill(@PathVariable Long gameID, @RequestBody KillDTOReg kill) throws InvalidBiteCodeException {
        Kill addedKill = killerService.createNewKill(gameID, mapper.regKillDTO(kill,gameID));
        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(mapper.toKillDTO(addedKill), status);
    }


    @Operation(tags = "Kill", summary = "Update a kill", description = "Method for updating a kill in a game. Admin or the player who created the kill.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kill updated.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = KillDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Kill registration json incorrect. Check that the BiteCode is correct.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "401", description = "User not logged in.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "403", description = "User is not the killer or an admin.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "404", description = "No game with that gameID or kill with that killID.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = KillDTOReg.class)))
    @PutMapping("/{killID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<KillDTO> updateKill(@PathVariable Long gameID, @PathVariable Long killID, @RequestBody KillDTOReg kill, @AuthenticationPrincipal Jwt principal) throws AppUserNotFoundException, MissingPermissionsException {
        Kill unchangedKill = killerService.getSpecificKill(gameID, killID);
        String userOpenId = principal.getClaimAsString("sub");
        // Checks if the user is authorized as admin or the killer
        if (!(SecurityUtils.isAdmin(principal.getTokenValue()) || unchangedKill.getKiller().getUser().equals( appUserService.getSpecificUser(userOpenId) ))) {
            throw new MissingPermissionsException("User does not have the right permissions for this operation");
        }
        Kill updatedKill = killerService.updateKill(gameID, killID, kill);
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(mapper.toKillDTO(updatedKill), status);
    }


    @Operation(tags = "Kill", summary = "Delete a kill", description = "Method for deleting a kill in a game.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kill deleted.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = KillDTO.class)) }),
            @ApiResponse(responseCode = "401", description = "User not logged in.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "403", description = "User is not an admin.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "404", description = "No game with that gameID or kill with that killID.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
    })
    @DeleteMapping("/{killID}")
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    public ResponseEntity<KillDTO> deleteKill(@PathVariable Long gameID, @PathVariable Long killID) {
        Kill deletedKill = killerService.deleteKill(gameID, killID);
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(mapper.toKillDTO(deletedKill), status);
    }
}
