package no.noroff.hvz.services;

import no.noroff.hvz.exceptions.AppUserAlreadyExistException;
import no.noroff.hvz.exceptions.AppUserNotFoundException;
import no.noroff.hvz.exceptions.MissingPlayerException;
import no.noroff.hvz.models.AppUser;
import no.noroff.hvz.models.Game;
import no.noroff.hvz.models.Player;
import no.noroff.hvz.repositories.AppUserRepository;
import no.noroff.hvz.repositories.GameRepository;
import no.noroff.hvz.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private PlayerRepository playerRepository;

    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }

    /**
     * Method for getting a specific user from the DB
     * @param openId auth0 id
     * @return The requested AppUser
     * @throws AppUserNotFoundException when appUser is not found
     */
    public AppUser getSpecificUser(String openId) throws AppUserNotFoundException {
        if(!appUserRepository.existsAppUserByOpenId(openId)) {
            throw new AppUserNotFoundException("Could not find user");
        }
        return appUserRepository.getAppUserByOpenId(openId);
    }

    /**
     * Method for getting a player based on the user and game
     * @param gameId id of game
     * @param user user we want player from
     * @return The wanted player object
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public Player getPlayerByGameAndUser(Long gameId, AppUser user) throws MissingPlayerException {
        Game game = gameRepository.findById(gameId).get();
        if(!playerRepository.existsByGameAndUser(game,user)) throw new MissingPlayerException("Player not found");
        return playerRepository.getPlayerByGameAndUser(game,user);
    }

    /**
     * Method for adding a user to the DB
     * @param addedUser the user to be added
     * @return Saved object/error
     * @throws AppUserAlreadyExistException when the user already exists
     */
    public AppUser createUser(AppUser addedUser) throws AppUserAlreadyExistException {
        if (appUserRepository.existsAppUserByOpenId(addedUser.getOpenId())) throw new AppUserAlreadyExistException("User already exists");
        addedUser.setPlayers(new HashSet<>());
        return appUserRepository.save(addedUser);
    }
}
