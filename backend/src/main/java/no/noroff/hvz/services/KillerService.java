package no.noroff.hvz.services;

import no.noroff.hvz.dto.kill.KillDTOReg;
import no.noroff.hvz.mapper.CustomMapper;
import no.noroff.hvz.models.Kill;
import no.noroff.hvz.repositories.GameRepository;
import no.noroff.hvz.repositories.KillerRepository;
import no.noroff.hvz.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class KillerService {

    @Autowired
    private KillerRepository killerRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private CustomMapper mapper;

    /**
     * Method for getting all kills in the DB
     * @param gameID id of game
     * @return List of kills
     */
    public List<Kill> getAllKills(Long gameID) {
        if(!gameRepository.existsById(gameID)) {
            throw new NoSuchElementException("Could not find game with id: " + gameID);
        }
        return killerRepository.getKillsByGame_Id(gameID);
    }

    /**
     * Method for getting a players kills
     * @param gameID id of game
     * @param killerID id of killer
     * @return List of kills
     */
    public List<Kill> getAllKills(Long gameID, Long killerID) {
        if(!gameRepository.existsById(gameID)) {
            throw new NoSuchElementException("Could not find game with id: " + gameID);
        }
        return killerRepository.getKillsByGame_IdAndKiller_Id(gameID, killerID);
    }

    /**
     * Method for getting a specific kill
     * @param gameID id of game
     * @param killID id of kill
     * @return The requested kill
     */
    public Kill getSpecificKill( Long gameID, Long killID) {
        if (!gameRepository.existsById(gameID)) throw new NoSuchElementException();
        return killerRepository.findById(killID).get();
    }

    /**
     * Method for creating a new kill
     * @param gameID id of game
     * @param kill kill to be saved in database
     * @return Yhe saved kill/error
     */
    public Kill createNewKill(Long gameID, Kill kill) {
        kill.setGame(gameRepository.findById(gameID).get());
        kill.getVictim().setHuman(false);
        if (kill.getTimeOfDeath() == null) kill.setTimeOfDeath(new Date());
        return killerRepository.save(kill);
    }

    /**
     * Method for updating a kill
     * @param gameID id of game
     * @param killID id of kill
     * @param killDto kill to be updated
     * @return The updated kill
     */
    public Kill updateKill(Long gameID, Long killID, KillDTOReg killDto) {
        Kill updatedKill = getSpecificKill(gameID, killID);
        mapper.updateKillFromDto(killDto, updatedKill);
        if (killDto.getKillerID() != null) updatedKill.setKiller(playerRepository.findById(killDto.getKillerID()).get());
        if (killDto.getBiteCode() != null) updatedKill.setVictim(playerRepository.getPlayerByGame_IdAndBiteCode(gameID, killDto.getBiteCode()));
        return killerRepository.save(updatedKill);
    }

    /**
     * Method for deleting a kill
     * @param gameID id of game
     * @param killID id of kill
     * @return The deleted kill
     */
    public Kill deleteKill(Long gameID, Long killID) {
        if (!gameRepository.existsById(gameID)) throw new NoSuchElementException();
        Kill deletedKill = killerRepository.findById(killID).get();
        killerRepository.deleteById(killID);
        return deletedKill;
    }
}
