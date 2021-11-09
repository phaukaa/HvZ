package no.noroff.hvz.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import no.noroff.hvz.dto.user.AppUserDTOFull;
import no.noroff.hvz.dto.user.AppUserDTOReg;
import no.noroff.hvz.exceptions.AppUserAlreadyExistException;
import no.noroff.hvz.exceptions.AppUserNotFoundException;
import no.noroff.hvz.mapper.Mapper;
import no.noroff.hvz.models.AppUser;
import no.noroff.hvz.security.SecurityUtils;
import no.noroff.hvz.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class AppUserController {

    @Autowired
    private Mapper mapper;
    @Autowired
    private AppUserService appUserService;


    /**
     * Method for getting all users. Admin only.
     * @return List of appUser DTOs
     */
    @Operation(tags = "User", summary = "Get all users -ADMIN ONLY")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppUserDTOFull.class)) }),
            @ApiResponse(responseCode = "401", description = "User not logged in.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "403", description = "User is not an admin.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
    })
    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_admin:permissions')")
    public ResponseEntity<List<AppUserDTOFull>> getAllUsers() {
        List<AppUser> users= appUserService.getAllUsers();
        List<AppUserDTOFull> userDTOs = users.stream().map(user -> mapper.toAppUserDTOFull(user, false)).collect(Collectors.toList());
        return  new ResponseEntity<>(userDTOs,HttpStatus.OK);
    }


    /**
     * Method for getting your user
     * @param principal Auth token with openID
     * @return the specific user DTO
     */
    @Operation(tags = "User", summary = "Get current user", description = "Method for getting the current user from the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppUserDTOFull.class)) }),
            @ApiResponse(responseCode = "401", description = "User not logged in.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "404", description = "User does not exist.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
    })
    @GetMapping("/log-in")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AppUserDTOFull> getSpecificUser( @AuthenticationPrincipal Jwt principal) {
        AppUser appUser;
        // try catch because we send a different Http status code than usually with AppUserNotFoundException
        try {
            appUser = appUserService.getSpecificUser(principal.getClaimAsString("sub"));
        } catch (AppUserNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        HttpStatus status = HttpStatus.OK;
        return new ResponseEntity<>(mapper.toAppUserDTOFull(appUser, SecurityUtils.isAdmin(principal.getTokenValue())), status);
    }


    /**
     * Method for creating a new user
     * @param userDTO DTO for a new user
     * @param principal Auth token
     * @return the created user DTO
     * @throws DataIntegrityViolationException when the user already exists
     */
    @Operation(tags = "User", summary = "Create user", description = "Method for creating a user in the database for the current user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Users created.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppUserDTOFull.class)) }),
            @ApiResponse(responseCode = "400", description = "User registration json incorrect.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "401", description = "User not logged in.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
            @ApiResponse(responseCode = "409", description = "User have already created a user.",
                    content = @Content(mediaType = "Text", schema = @Schema(description = "Error message"))),
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppUserDTOReg.class)))
    @PostMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AppUserDTOFull> createUser(@RequestBody AppUserDTOReg userDTO, @AuthenticationPrincipal Jwt principal) throws DataIntegrityViolationException, AppUserAlreadyExistException {
        AppUser appUser = mapper.toAppUser(userDTO);
        appUser.setOpenId(principal.getClaimAsString("sub"));
        AppUser addedUser = appUserService.createUser(appUser);
        HttpStatus status = HttpStatus.CREATED;
        return new ResponseEntity<>(mapper.toAppUserDTOFull(addedUser, SecurityUtils.isAdmin(principal.getTokenValue())), status);
    }
}
