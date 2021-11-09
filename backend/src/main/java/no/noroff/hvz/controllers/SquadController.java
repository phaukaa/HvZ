package no.noroff.hvz.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import no.noroff.hvz.dto.message.MessageDTO;
import no.noroff.hvz.dto.message.MessageDTOreg;
import no.noroff.hvz.dto.player.PlayerDTOFull;
import no.noroff.hvz.dto.squad.*;
import no.noroff.hvz.exceptions.AppUserNotFoundException;
import no.noroff.hvz.exceptions.MissingPermissionsException;
import no.noroff.hvz.exceptions.MissingPlayerException;
import no.noroff.hvz.mapper.Mapper;
import no.noroff.hvz.models.*;
import no.noroff.hvz.security.SecurityUtils;
import no.noroff.hvz.services.SquadService;
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
@RequestMapping("/api/game/{gameID}/squad")
@CrossOrigin(origins = "*")
public class SquadController {

    @Autowired
    private SquadService squadService;
    @Autowired
    private Mapper mapper;
    @Autowired
    private AppUserService appUserService;

    private HttpStatus status;


    @Operation(tags = "Squad", summary = "Get all squads", description = "Method for getting all squads in a game. Optional player id returns that players squad instead.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Squads found.",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = SquadDTO.class))) }),
            @ApiResponse(responseCode = "401", description = "User not logged in.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "404", description = "No game with that ID.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
    })
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<SquadDTO>> getAllSquads(@PathVariable Long gameID, @RequestParam Optional<String> playerId) {
        List<SquadDTO> squadDTOs = new ArrayList<>();
        List<Squad> squads;
        if (playerId.isPresent()) {
            Squad squad = squadService.getSquadByPlayer(gameID, Long.parseLong(playerId.get()));
            SquadDTO squadDTO = mapper.toSquadDTO(squad);
            squadDTOs.add(squadDTO);
        }
        else {
            squads = squadService.getAllSquads(gameID);
            squadDTOs = squads.stream().map(mapper::toSquadDTO).collect(Collectors.toList());
        }

        return new ResponseEntity<>(squadDTOs,HttpStatus.OK);
    }


    @Operation(tags = "Squad", summary = "Get a specific squad", description = "Method for getting a squad in a game.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Squad found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SquadDTO.class)) }),
            @ApiResponse(responseCode = "401", description = "User not logged in.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "404", description = "No game with that gameID, or no squad with that squadID.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
    })
    @GetMapping("/{squadID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SquadDTO> getSpecificSquad(@PathVariable Long gameID, @PathVariable Long squadID) {
        Squad squad = squadService.getSpecificSquad(gameID, squadID);
        SquadDTO squadDTO = mapper.toSquadDTO(squad);
        status = HttpStatus.OK;
        return new ResponseEntity<>(squadDTO, status);
    }


    @Operation(tags = "Squad", summary = "Create a squad", description = "Method for creating a squad in a game. If created by a player, the player is added as first member.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Squad created.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SquadDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Squad requestbody json incorrect.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "401", description = "User not logged in.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "404", description = "No game with that gameID.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = SquadDTOReg.class)))
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SquadDTO> createNewSquad(@PathVariable Long gameID, @RequestBody SquadDTOReg squad,
                                                   @AuthenticationPrincipal Jwt principal) throws AppUserNotFoundException, MissingPermissionsException {
        AppUser user = appUserService.getSpecificUser(principal.getClaimAsString("sub"));
        Squad createdSquad;

        Player player;
        // Try catch is here because we do not return the usual Http status for NoSuchElementException.
        try {
            player = appUserService.getPlayerByGameAndUser(gameID, user);
        } catch (MissingPlayerException e) {
            throw new MissingPermissionsException("User is not a player in this game");
        }
        createdSquad = squadService.createNewSquad(gameID, mapper.toSquad(squad), player);

        status = HttpStatus.CREATED;
        SquadDTO squadDTO = mapper.toSquadDTO(createdSquad);
        return new ResponseEntity<>(squadDTO, status);
    }


    @Operation(tags = "Squad", summary = "Join a squad", description = "Method for joining a squad in a game.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Squad joined successfully.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SquadDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Squadmember requestbody json incorrect.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "401", description = "User not logged in.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "404", description = "No game with that gameID, or no squad with that squadID",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "409", description = "Player is already a member of this squad.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = SquadMemberFromDTO.class)))
    @PostMapping("/{squadID}/join")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SquadDTO> joinSquad(@PathVariable Long gameID, @PathVariable Long squadID, @RequestBody SquadMemberFromDTO member) {
        SquadMember addedSquadMember = squadService.joinSquad(gameID, squadID, mapper.toSquadMember(member, gameID));
        SquadDTO squadDTO = mapper.toSquadDTO(squadService.getSpecificSquad(gameID, addedSquadMember.getSquad().getId()));
        status = HttpStatus.CREATED;
        return new ResponseEntity<>(squadDTO, status);
    }


    @Operation(tags = "Squad", summary = "Update a squad", description = "Method for updating a squad in a game. Admin only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Squad updated successfully.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SquadDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Squad requestbody json incorrect.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "401", description = "User not logged in.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "403", description = "User is not an admin.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "404", description = "No game with that gameID, or no squad with that squadID",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = SquadDTOUpdate.class)))
    @PutMapping("/{squadID}")
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    public ResponseEntity<SquadDTO> updateSquad(@PathVariable Long gameID, @PathVariable Long squadID, @RequestBody SquadDTOUpdate squadDTO) {
        Squad updatedSquad = squadService.updateSquad(gameID, squadID, squadDTO);
        status = HttpStatus.OK;
        return new ResponseEntity<>(mapper.toSquadDTO(updatedSquad), status);
    }


    @Operation(tags = "Squad", summary = "Delete a squad", description = "Method for deleting a squad in a game. Admin only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Squad deleted.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SquadDTO.class)) }),
            @ApiResponse(responseCode = "401", description = "User not logged in.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "403", description = "User is not an admin.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "404", description = "No game with that gameID or squad with that squadID.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
    })
    @DeleteMapping("/{squadID}")
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    public ResponseEntity<SquadDTO> deleteSquad(@PathVariable Long gameID, @PathVariable Long squadID) {
        Squad deletedSquad = squadService.deleteSquad(gameID, squadID);
        status = HttpStatus.OK;
        SquadDTO squadDTO = mapper.toSquadDTO(deletedSquad);
        return new ResponseEntity<>(squadDTO, status);
    }


    @Operation(tags = "Chat", summary = "Get squadchat", description = "Method for getting the chat in a squad in a game. Players will only get messages if they are in the squad and matches the squads faction.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Squadchat found.",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = MessageDTO.class))) }),
            @ApiResponse(responseCode = "401", description = "User not logged in.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "404", description = "No game with that ID, or squad with that squadID",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
    })
    @GetMapping("/{squadID}/chat")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MessageDTO>> getSquadChat(@PathVariable Long gameID, @PathVariable Long squadID,
                                                         @RequestHeader String authorization, @AuthenticationPrincipal Jwt principal) throws AppUserNotFoundException, MissingPermissionsException {
        List<MessageDTO> chatDTO;
        List<Message> chat = squadService.getSquadChat(gameID, squadID);

        if(SecurityUtils.isAdmin(authorization)) {
            chatDTO = chat.stream().map(mapper::toMessageDTO).collect(Collectors.toList());
            status = HttpStatus.OK;
        }
        else {
            AppUser appUser = appUserService.getSpecificUser(principal.getClaimAsString("sub"));
            Player player = appUserService.getPlayerByGameAndUser(gameID, appUser);
            Squad squad= squadService.getSpecificSquad(gameID, squadID);
            //check if player
            if(player.isHuman() == squad.isHuman() && squadService.isMemberOfSquad(squad,player)) {
                chatDTO = chat.stream().map(mapper::toMessageDTO).collect(Collectors.toList());
                status = HttpStatus.OK;
            }
            else {
                throw new MissingPermissionsException("User do not have the right permissions for this operation.");
            }
        }
        return new ResponseEntity<>(chatDTO, status);
    }


    @Operation(tags = "Squad", summary = "Post a new squadmessage", description = "Method for creating a new message in the squad chat. Players must be part of the squad and the correct faction.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Squadmessage joined successfully.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Squadmessage requestbody json incorrect.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "401", description = "User not logged in.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "404", description = "No game with that gameID, or no squad with that squadID",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = SquadMemberFromDTO.class)))
    @PostMapping("/{squadID}/chat")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageDTO> createSquadChat(@PathVariable Long gameID, @PathVariable Long squadID, @RequestBody MessageDTOreg message, @AuthenticationPrincipal Jwt principal) throws AppUserNotFoundException, MissingPermissionsException {
        AppUser appUser = appUserService.getSpecificUser(principal.getClaimAsString("sub"));
        if(!SecurityUtils.isAdmin(principal.getTokenValue())) {
            try {
                Player player = appUserService.getPlayerByGameAndUser(gameID, appUser);
                Squad squad= squadService.getSpecificSquad(gameID, squadID);
                if(player.isHuman() != squad.isHuman() || !squadService.isMemberOfSquad(squad,player)) {
                    throw new MissingPermissionsException("Player is not allowed to post messages in this squad");
                }
            } catch (NoSuchElementException e) {
                throw new MissingPermissionsException("User is not a member of this squad.");
            }
        }
        Message chat = squadService.createSquadChat(gameID, squadID, appUser, mapper.toMessage(message));
        status = HttpStatus.CREATED;
        MessageDTO messageDTO = mapper.toMessageDTO(chat);
        return new ResponseEntity<>(messageDTO, status);
    }


    @Operation(tags = "Squad", summary = "Get squad check-in", description = "Method for getting all squadCheckIns for a squad in a game. Players must be part of the squad and the correct faction.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Check-ins found.",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = SquadCheckInDTO.class))) }),
            @ApiResponse(responseCode = "401", description = "User not logged in.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "403", description = "User is not a member of this squad.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "404", description = "No game with that ID, or squad with that squadID",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
    })
    @GetMapping("/{squadID}/check-in")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<SquadCheckInDTO>> getSquadCheckIn(@PathVariable Long gameID, @PathVariable Long squadID,
                                                                 @AuthenticationPrincipal Jwt principal) throws AppUserNotFoundException, MissingPermissionsException {
        List<SquadCheckInDTO> checkInDTOs;
        List<SquadCheckIn> checkins = squadService.getSquadCheckIn(gameID, squadID);
        if(SecurityUtils.isAdmin(principal.getTokenValue())) {
            checkInDTOs = checkins.stream().map(mapper::toSquadCheckInDTO).collect(Collectors.toList());
            status = HttpStatus.OK;
        }
        else {
            AppUser appUser = appUserService.getSpecificUser(principal.getClaimAsString("sub"));
            Player player = appUserService.getPlayerByGameAndUser(gameID, appUser);
            Squad squad= squadService.getSpecificSquad(gameID, squadID);
            //check if player
            if(player.isHuman() == squad.isHuman() && squadService.isMemberOfSquad(squad,player)) {
                checkInDTOs = checkins.stream().map(mapper::toSquadCheckInDTO).collect(Collectors.toList());
                status = HttpStatus.OK;
            }
            else {
                throw new MissingPermissionsException("User is not a member of this squad.");
            }
        }
        return new ResponseEntity<>(checkInDTOs, status);
    }


    @Operation(tags = "Squad", summary = "Create a new squad-check-in", description = "Method for creating a squadCheckIn for a squad in a game. User must be part of the squad and the correct faction.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "squad-check-in created successfully.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SquadCheckInDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "squad-check-in requestbody json incorrect.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "401", description = "User not logged in.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "403", description = "Player is not a member of this squad.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "404", description = "No game with that gameID, or no squad with that squadID",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = SquadCheckInDTOReg.class)))
    @PostMapping("/{squadID}/check-in")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SquadCheckInDTO> createSquadCheckIn(@PathVariable Long gameID, @PathVariable Long squadID,
                                                              @RequestBody SquadCheckInDTOReg checkInDTO, @AuthenticationPrincipal Jwt principal) throws AppUserNotFoundException, MissingPermissionsException {
        SquadCheckInDTO addedCheckInDTO;
        AppUser appUser = appUserService.getSpecificUser(principal.getClaimAsString("sub"));
        Player player = appUserService.getPlayerByGameAndUser(gameID, appUser);
        Squad squad= squadService.getSpecificSquad(gameID, squadID);
        //check if player and squad is same faction or admin
        if((player.isHuman() == squad.isHuman() && squadService.isMemberOfSquad(squad,player)) ||
                SecurityUtils.isAdmin(principal.getTokenValue())) {
            SquadCheckIn addedCheckIn = squadService.createSquadCheckIn(gameID, squadID, mapper.toSquadCheckIn(checkInDTO, gameID));
            status = HttpStatus.OK;
            addedCheckInDTO = mapper.toSquadCheckInDTO(addedCheckIn);
        }
        else {
            throw new MissingPermissionsException("User is not a member of this squad.");
        }
        return new ResponseEntity<>(addedCheckInDTO, status);
    }


    @Operation(tags = "Squad", summary = "Leave a squad", description = "Method for leaving a squad in a game.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Left squad successfully.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(oneOf = {PlayerDTOFull.class, SquadMemberDTO.class})) }),
            @ApiResponse(responseCode = "400", description = "User is not a member of this squad.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "401", description = "User not logged in.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "404", description = "No game with that gameID or player with that playerID.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
    })
    @DeleteMapping("/{squadID}/leave")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SquadMemberDTO> leaveSquad(@PathVariable Long gameID, @PathVariable Long squadID, @AuthenticationPrincipal Jwt principal) throws AppUserNotFoundException, MissingPermissionsException {
        AppUser appUser = appUserService.getSpecificUser(principal.getClaimAsString("sub"));
        Player player = appUserService.getPlayerByGameAndUser(gameID, appUser);
        SquadMember leaver = squadService.leaveSquad(gameID,squadID,player);
        SquadMemberDTO squadMemberDTO = mapper.toSquadMemberDTO(leaver);
        status = HttpStatus.OK;
        return new ResponseEntity<>(squadMemberDTO, status);
    }
}
