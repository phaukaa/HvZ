package no.noroff.hvz.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import no.noroff.hvz.dto.player.*;
import no.noroff.hvz.exceptions.AppUserNotFoundException;
import no.noroff.hvz.mapper.Mapper;
import no.noroff.hvz.models.AppUser;
import no.noroff.hvz.models.Player;
import no.noroff.hvz.security.SecurityUtils;
import no.noroff.hvz.services.AppUserService;
import no.noroff.hvz.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/game/{gameID}/player")
@CrossOrigin(origins = "*")
public class PlayerController {

    @Autowired
    private PlayerService playerService;
    @Autowired
    private Mapper mapper;
    @Autowired
    private AppUserService appUserService;

    /**
     * Method for getting all players
     * @param gameID ID of game
     * @param principal Auth token
     * @return list of players
     */
    @Operation(tags = "Player", summary = "Get all players", description = "Method for getting all players in a game. Players get limited information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Players found.",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(oneOf = {PlayerDTOFull.class, PlayerDTOStandard.class}))) }),
            @ApiResponse(responseCode = "401", description = "User not logged in.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "404", description = "No game with that ID.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
    })
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PlayerDTO>> getAllPlayers(@PathVariable Long gameID, @AuthenticationPrincipal Jwt principal) {
        List<PlayerDTO> playerDTOs;
        Set<Player> players = playerService.getAllPlayers(gameID);
        // Checks if user is admin or not
        if (SecurityUtils.isAdmin(principal.getTokenValue())) {
            playerDTOs = players.stream().map(mapper::toPlayerDTOFull).collect(Collectors.toList());
        }
        else {
            playerDTOs = players.stream().map(mapper::toPlayerDTOStandard).collect(Collectors.toList());
        }
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(playerDTOs, status);
    }


    /**
     * Method for getting a specific player
     * @param gameID ID of game
     * @param playerID ID of player
     * @param principal Auth token
     * @return the player
     */
    @Operation(tags = "Player", summary = "Get a specific player", description = "Method for getting a specific player in a game. Players get limited information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(oneOf = {PlayerDTOFull.class, PlayerDTOStandard.class})) }),
            @ApiResponse(responseCode = "401", description = "User not logged in.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "404", description = "No game with that gameID, or no player with that playerID.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
    })
    @GetMapping("/{playerID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PlayerDTO> getSpecificPlayer(@PathVariable Long gameID, @PathVariable Long playerID, @AuthenticationPrincipal Jwt principal) {
        Player player = playerService.getSpecificPlayer(gameID, playerID);
        PlayerDTO playerDTO;
        // Checks if user is admin
        if (SecurityUtils.isAdmin(principal.getTokenValue())) {
            playerDTO = mapper.toPlayerDTOFull(player);
        }
        else {
            playerDTO = mapper.toPlayerDTOStandard(player);
        }
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(playerDTO, status);
    }


    /**
     * Method for creating a new player, different for an admin
     * @param gameID ID of game
     * @param player Optional DTO with info about the new player
     * @param principal Auth token
     * @return the created player
     * @throws AppUserNotFoundException if the provided user do not exists
     */
    @Operation(tags = "Player", summary = "Create a player", description = "Method for a new player in a game. Admins can create players without default values.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Player created.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(oneOf = {PlayerDTOFull.class, PlayerDTOStandard.class})) }),
            @ApiResponse(responseCode = "400", description = "Player registration json incorrect.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "401", description = "User not logged in.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "404", description = "No game with that gameID.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlayerDTORegAdmin.class)))
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PlayerDTO> createNewPlayer(@PathVariable Long gameID, @RequestBody Optional<PlayerDTORegAdmin> player,
                                                      @AuthenticationPrincipal Jwt principal) throws AppUserNotFoundException {
        HttpStatus status;
        Player newPlayer;
        PlayerDTO playerDTO;
        //Admins can specify some fields, while a normal user gets a default player
        if(SecurityUtils.isAdmin(principal.getTokenValue())) {
            newPlayer = playerService.createNewPlayer(gameID, mapper.regPlayerDTO( player.get()));
            playerDTO = mapper.toPlayerDTOFull(newPlayer);
        }
        else {
            AppUser user = appUserService.getSpecificUser(principal.getClaimAsString("sub"));
            newPlayer = playerService.createNewPlayer(gameID, user);
            playerDTO = mapper.toPlayerDTOStandard(newPlayer);
        }
        status = HttpStatus.CREATED;
        return new ResponseEntity<>(playerDTO,status);
    }


    /**
     * Method for updating a player, admin only
     * @param gameID ID of game
     * @param playerID ID of player
     * @param playerDTO DTO with new info
     * @return the updated player
     */
    @Operation(tags = "Player", summary = "Update a player", description = "Method for updating a player in a game. Admin only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player updated.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(oneOf = {PlayerDTOFull.class, PlayerDTOStandard.class})) }),
            @ApiResponse(responseCode = "400", description = "Player requestBody json incorrect.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "401", description = "User not logged in.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "403", description = "User is not admin.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "404", description = "No game with that gameID, or player with that PlayerID",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlayerDTOUpdate.class)))
    @PutMapping("/{playerID}")
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    public ResponseEntity<PlayerDTO> updatePlayer(@PathVariable Long gameID, @PathVariable Long playerID,
                                                  @RequestBody PlayerDTOUpdate playerDTO) {
        HttpStatus status;
        Player updatedPlayer = playerService.updatePlayer(gameID, playerID, playerDTO);
        PlayerDTO updatedPlayerDTO = mapper.toPlayerDTOFull(updatedPlayer);
        status = HttpStatus.OK;
        return new ResponseEntity<>(updatedPlayerDTO, status);
    }


    /**
     * Method for deleting a player, admin only
     * @param gameID ID of game
     * @param playerID ID of player
     * @return the deleted player
     */
    @Operation(tags = "Player", summary = "Delete a player", description = "Method for deleting a player in a game. Admin only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player deleted.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(oneOf = {PlayerDTOFull.class, PlayerDTOStandard.class})) }),
            @ApiResponse(responseCode = "401", description = "User not logged in.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "403", description = "User is not an admin.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "404", description = "No game with that gameID or player with that playerID.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
    })
    @DeleteMapping("/{playerID}")
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    public ResponseEntity<PlayerDTO> deletePlayer(@PathVariable Long gameID, @PathVariable Long playerID) {
        Player deletedPlayer = playerService.deletePlayer(gameID, playerID);
        HttpStatus status = HttpStatus.OK;
        PlayerDTO playerDTO = mapper.toPlayerDTOFull(deletedPlayer);
        return new ResponseEntity<>(playerDTO, status);
    }
}
