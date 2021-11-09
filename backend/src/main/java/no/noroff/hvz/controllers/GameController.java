package no.noroff.hvz.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import no.noroff.hvz.dto.game.GameDTO;
import no.noroff.hvz.dto.game.GameDTOReg;
import no.noroff.hvz.dto.game.GameDTOUpdate;
import no.noroff.hvz.dto.message.MessageDTO;
import no.noroff.hvz.dto.message.MessageDTOreg;
import no.noroff.hvz.exceptions.AppUserNotFoundException;
import no.noroff.hvz.mapper.Mapper;
import no.noroff.hvz.models.AppUser;
import no.noroff.hvz.models.Game;
import no.noroff.hvz.models.Message;
import no.noroff.hvz.models.Player;
import no.noroff.hvz.security.SecurityUtils;
import no.noroff.hvz.services.GameService;
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
@RequestMapping("/api/game")
// NB! Configure origins properly before deploy!
@CrossOrigin(origins = "*")
public class GameController {

    @Autowired
    private GameService gameService;
    @Autowired
    AppUserService appUserService;
    @Autowired
    private Mapper mapper;


    @Operation(tags = "Game", summary = "Get all games", description = "API for getting all games, optional parameter state -> only returns games with provided state.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Games found.",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GameDTO.class))) }),
    })
    @GetMapping
    public ResponseEntity<List<GameDTO>> getAllGames(@RequestParam Optional<String> state) {
        List<GameDTO> games;
        games = state.map(s -> gameService.getAllGames(s).stream().map(mapper::toGameDTO).collect(Collectors.toList())).orElseGet(() -> gameService.getAllGames().stream().map(mapper::toGameDTO).collect(Collectors.toList()));
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(games, status);
    }


    @Operation(tags = "Game", summary = "Get game by ID", description = "Returns a specific game.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GameDTO.class)) }),
            @ApiResponse(responseCode = "401", description = "User not logged in.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "404", description = "Game not found.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
    })
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GameDTO> getSpecificGame(@PathVariable Long id) throws NoSuchElementException {

        Game game = gameService.getSpecificGame(id);
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(mapper.toGameDTO(game),status);
    }


    @Operation(tags = "Game", summary = "Create game -ADMIN ONLY", description = "Creates a new game from details provided in the request body. Admin only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Game created.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GameDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Json body incorrect.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "401", description = "User not logged in.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "403", description = "User is not Admin.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = GameDTOReg.class)))
    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    public ResponseEntity<GameDTO> createNewGame(@RequestBody GameDTOReg gameDTOReg) {
        Game game = mapper.toGame(gameDTOReg);
        Game addedGame = gameService.createNewGame(game);
        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(mapper.toGameDTO(addedGame), status);
    }


    @Operation(tags = "Game", summary = "Update game -ADMIN ONLY", description = "Method for updating a game. Admin only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game changed.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GameDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Json body incorrect.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "401", description = "User not logged in.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "403", description = "User is not Admin.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "404", description = "No game with that ID.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = GameDTOUpdate.class)))
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    public ResponseEntity<GameDTO> updateSpecificGame(@PathVariable Long id, @RequestBody GameDTOUpdate gameDTO) {
        HttpStatus status = HttpStatus.OK;
        Game game = mapper.toGame(gameDTO, id);
        Game updatedGame = gameService.updateSpecificGame(id, game);
        return new ResponseEntity<>(mapper.toGameDTO(updatedGame),status);
    }


    @Operation(tags = "Game", summary = "Delete game -ADMIN ONLY", description = "Method for deleting a game. Admin only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game deleted.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GameDTO.class)) }),
            @ApiResponse(responseCode = "401", description = "User not logged in.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "403", description = "User is not Admin.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "404", description = "No game with that ID.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    public ResponseEntity<GameDTO> deleteGame(@PathVariable Long id) {
        Game deletedGame = gameService.deleteGame(id);
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(mapper.toGameDTO(deletedGame), status);
    }


    @Operation(tags = "Chat", summary = "Get game chat by gameID", description = "Method for getting chat for a game. Admins get all messages, " +
            "players get global and faction messages. Optional playerID returns the players messages. " +
            "Optional faction boolean returns only that factions messages.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chat found.",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = MessageDTO.class))) }),
            @ApiResponse(responseCode = "400", description = "Request parameters incorrect",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "401", description = "User not logged in.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "404", description = "Game not found.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
    })
    @GetMapping("/{id}/chat")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MessageDTO>> getGameChat(@PathVariable Long id,
                                                     @RequestHeader(required = false) String playerID,
                                                     @RequestHeader(required = false) String human,
                                                     @AuthenticationPrincipal Jwt principal
                                                     ) throws NullPointerException, AppUserNotFoundException {
        System.out.println(principal.getTokenValue());
        HttpStatus status;
        List<Message> messages;
        List<MessageDTO> messageDTOs;
        if(SecurityUtils.isAdmin(principal.getTokenValue())) {
            if (playerID != null) messages = gameService.getGameChat(id, Long.parseLong(playerID));
            else if (human != null) messages = gameService.getGameChat(id, Boolean.valueOf(human));
            else messages = gameService.getGameChat(id);
        }
        else {
            AppUser user = appUserService.getSpecificUser(principal.getClaimAsString("sub"));
            Player player = appUserService.getPlayerByGameAndUser(id, user);
            if (playerID != null && Long.parseLong(playerID) == player.getId()) messages = gameService.getGameChat(id, Long.parseLong(playerID));
            else if (human != null) messages = gameService.getGameChat(id, player.isHuman());
            else messages = gameService.getGameChat(id);
        }
        status = HttpStatus.OK;
        messageDTOs = messages.stream().map(mapper::toMessageDTO).collect(Collectors.toList());

        return new ResponseEntity<>(messageDTOs,status);
    }


    @Operation(tags = "Chat", summary = "Create a message", description = "Method for posting a new message in game chat.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Message posted.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Json body incorrect.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "401", description = "User not logged in.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "404", description = "No game with that ID.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDTOreg.class)))
    @PostMapping("/{id}/chat")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageDTO> createNewChat(@PathVariable Long id, @RequestBody MessageDTOreg message, @AuthenticationPrincipal Jwt principal) throws AppUserNotFoundException {
        if (SecurityUtils.isAdmin(principal.getTokenValue())) {
            AppUser appUser = appUserService.getSpecificUser(principal.getClaimAsString("sub"));
            Message createdMessage = gameService.createNewAdminChat(id, mapper.toAdminMessage(message), appUser);
            HttpStatus status = HttpStatus.CREATED;
            return new ResponseEntity<>(mapper.toMessageDTO(createdMessage), status);
        } else {
            AppUser appUser = appUserService.getSpecificUser(principal.getClaimAsString("sub"));
            Message createdMessage = gameService.createNewChat(id, mapper.toMessage(message), appUser);
            HttpStatus status = HttpStatus.CREATED;
            return new ResponseEntity<>(mapper.toMessageDTO(createdMessage), status);
        }
    }
}
