package no.noroff.hvz.services;

import no.noroff.hvz.dto.player.PlayerDTOUpdate;
import no.noroff.hvz.exceptions.PlayerAlreadyExistException;
import no.noroff.hvz.mapper.CustomMapper;
import no.noroff.hvz.mapper.Mapper;
import no.noroff.hvz.models.AppUser;
import no.noroff.hvz.models.Game;
import no.noroff.hvz.models.Player;
import no.noroff.hvz.repositories.GameRepository;
import no.noroff.hvz.repositories.PlayerRepository;
import no.noroff.hvz.repositories.SquadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private SquadRepository squadRepository;
    @Autowired
    private Mapper mapper;
    @Autowired
    private CustomMapper customMapper;

    /**
     * Method for getting all players for a game
     * @param gameID ID og game
     * @return returns all players in the game
     */
    public Set<Player> getAllPlayers(Long gameID) {
        Game game = gameRepository.findById(gameID).get();
        return game.getPlayers();
    }

    /**
     * Method for getting a specific player
     * @param gameID ID of game
     * @param playerID ID of player
     * @return the specified player
     */
    public Player getSpecificPlayer( Long gameID, Long playerID) {
        if(!gameRepository.existsById(gameID)) throw new NoSuchElementException();
        return playerRepository.findById(playerID).get();
    }

    /**
     * Method for getting a user's player in a game
     * @param gameId ID of game
     * @param user user object
     * @return the user's player in the game
     */
    public Player getPlayerByGameAndUser(Long gameId, AppUser user) {
        Player player = null;
        Game game = gameRepository.getById(gameId);
        if(playerRepository.existsByGameAndUser(game,user)) {
            player = playerRepository.getPlayerByGameAndUser(game,user);
        }
        return player;
    }

    /**
     * Method for getting a player from biteCode
     * @param gameId ID of game
     * @param biteCode bitecode
     * @return the biteCodes player in the game
     */
    public Player getPlayerByGameAndBiteCode(Long gameId, String biteCode) {
        Player player = null;
        Game game = gameRepository.getById(gameId);
        if(playerRepository.existsByGameAndBiteCode(game,biteCode)) {
            player = playerRepository.getPlayerByGameAndBiteCode(game,biteCode);
        }
        return player;
    }

    /**
     * Method for putting in default info to a new user generated player
     * @param gameID id of game
     * @param player player to be saved in database
     * @exception NoSuchElementException when the game is not found
     * @return player
     */
    public Player createNewPlayer(Long gameID, Player player) {
        Game game = gameRepository.findById(gameID).get();
        if(playerRepository.existsByGameAndUser(game,player.getUser())) {
            throw new PlayerAlreadyExistException("The user already has a player in this game");
        }
        player.setGame(game);
        player.setBiteCode(createRandomBiteCode(10));
        player.setPatientZero(false);
        player = playerRepository.save(player);
        return player;
    }

    /**
     * Method that creates a default player
     * @param gameID ID of game
     * @param user The user that creates the player
     * @return the new player
     */
    public Player createNewPlayer(Long gameID, AppUser user) {
        Player player = new Player();
        if(gameRepository.existsById(gameID)) {
            // Set the players default info
            Game game = gameRepository.getById(gameID);
            if(playerRepository.existsByGameAndUser(game,user)) {
                throw new PlayerAlreadyExistException("The user already has a player in this game");
            }
            player.setUser(user);
            player.setHuman(true);
            player.setPatientZero(false);
            player.setGame(game);
            String randomBiteCode = createRandomBiteCode(10);
            // Create a random biteCode -> if the bitecode already exist creates a new one
            String finalRandomBiteCode = randomBiteCode;
            List<Player> existingBiteCode = playerRepository.findAll().stream().filter(p -> Objects.equals(p.getBiteCode(), finalRandomBiteCode)).collect(Collectors.toList());
            while (existingBiteCode.size() > 0) {
                randomBiteCode = createRandomBiteCode(10);
                String finalRandomBiteCode1 = randomBiteCode;
                existingBiteCode = playerRepository.findAll().stream().filter(p -> Objects.equals(p.getBiteCode(), finalRandomBiteCode1)).collect(Collectors.toList());
            }
            player.setBiteCode(randomBiteCode);
            player = playerRepository.save(player);
        }
        return player;
    }


    /**
     * Method for creating a random biteCode
     * @param len length
     * @return biteCode
     */
    public static String createRandomBiteCode(int len) {
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int v = 1 + (int) (Math.random() * 26);
            char c = (char) (v + 'a' - 1);
            name.append(c);
        }
        return name.toString();
    }

    /**
     * Method for updating a player
     * @param gameID ID of game
     * @param playerID ID of player
     * @param playerDto player object with new info
     * @return the updated player
     */
    public Player updatePlayer(Long gameID, Long playerID, PlayerDTOUpdate playerDto) {
        if(!gameRepository.existsById(gameID)) throw new NoSuchElementException();
        Player player = playerRepository.findById(playerID).get();
        customMapper.updatePlayerFromDto(playerDto, player);
        return playerRepository.save(player);
    }

    /**
     * Method for deleting player
     * @param gameID ID of game
     * @param playerID ID of player
     * @return the deleted player
     */
    public Player deletePlayer(Long gameID, Long playerID) {
        if(!gameRepository.existsById(gameID)) throw new NoSuchElementException();
        Player deletedPlayer = playerRepository.findById(playerID).get();
        playerRepository.deleteById(playerID);
        return deletedPlayer;
    }
}
