package no.noroff.hvz.services;

import no.noroff.hvz.models.Game;
import no.noroff.hvz.models.Mission;
import no.noroff.hvz.repositories.GameRepository;
import no.noroff.hvz.repositories.MissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MissionService {

    @Autowired
    private MissionRepository missionRepository;
    @Autowired
    private GameRepository gameRepository;

    /**
     * Method for getting all missions in a game
     * @param gameID ID of game
     * @return List of missions in game
     */
    public List<Mission> getAllMissions(Long gameID) {
        List<Mission> missions = null;
        if(gameRepository.existsById(gameID)) {
            Game game = gameRepository.getById(gameID);
            missions = new ArrayList<>(game.getMissions());
        }
        return missions;
    }

    /**
     * Method for getting all missions for a faction in a game
     * @param gameID ID of game
     * @param isHuman true for human faction
     * @return list of faction missions in game
     */
    public List<Mission> getAllMissionsFaction(Long gameID, boolean isHuman) {
        List<Mission> missions = null;
        if(gameRepository.existsById(gameID)) {
            Game game = gameRepository.getById(gameID);
            //filters and only gets the mission which match the faction
            missions = game.getMissions().stream().filter(mission -> Objects.equals(mission.isHuman(),isHuman)).collect(Collectors.toList());
        }
        return missions;
    }

    /**
     * Method for getting a mission
     * @param gameID ID of game
     * @param missionID ID of mission
     * @return mission
     */
    public Mission getSpecificMission(Long gameID, Long missionID) {
        Mission mission = null;
        if(missionRepository.existsById(missionID) && gameRepository.existsById(gameID)) {
            mission = missionRepository.getById(missionID);
        }
        return mission;
    }

    /**
     * Method for creating a new mission
     * @param gameID ID of game
     * @param mission DTO with the new missions contents
     * @return the new mission
     */
    public Mission createNewMission(Long gameID, Mission mission) {
        Mission addedMission = new Mission();
        if(gameRepository.existsById(gameID)) {
            addedMission = missionRepository.save(mission);
        }
        return addedMission;
    }

    /**
     * Method for updating a mission
     * @param gameID ID of game
     * @param missionID ID of mission
     * @param mission DTO with updated contents
     * @return updated mission
     */
    public Mission updateMission( Long gameID, Long missionID, Mission mission) {
        Mission updatedMission = new Mission();
        if(gameRepository.existsById(gameID) && missionRepository.existsById(missionID)) {
            updatedMission = missionRepository.save(mission);
        }
        return updatedMission;
    }

    /**
     * Method for deleting a mission
     * @param gameID ID of game
     * @param missionID ID of mission
     * @return deleted mission
     */
    public Mission deleteMission(Long gameID, Long missionID) {
        if(!gameRepository.existsById(gameID)) throw new NoSuchElementException();
        Mission deletedMission = missionRepository.findById(missionID).get();
        missionRepository.deleteById(missionID);
        return deletedMission;
    }
}
