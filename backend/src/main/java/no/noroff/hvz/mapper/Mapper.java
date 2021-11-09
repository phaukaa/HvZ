package no.noroff.hvz.mapper;

import no.noroff.hvz.dto.game.GameDTO;
import no.noroff.hvz.dto.game.GameDTOReg;
import no.noroff.hvz.dto.game.GameDTOUpdate;
import no.noroff.hvz.dto.kill.KillDTO;
import no.noroff.hvz.dto.kill.KillDTOReg;
import no.noroff.hvz.dto.message.MessageDTO;
import no.noroff.hvz.dto.message.MessageDTOreg;
import no.noroff.hvz.dto.mission.MissionDTO;
import no.noroff.hvz.dto.mission.MissionDTOReg;
import no.noroff.hvz.dto.player.*;
import no.noroff.hvz.dto.squad.*;
import no.noroff.hvz.dto.user.AppUserDTOFull;
import no.noroff.hvz.dto.user.AppUserDTOReg;
import no.noroff.hvz.exceptions.AppUserNotFoundException;
import no.noroff.hvz.exceptions.InvalidBiteCodeException;
import no.noroff.hvz.models.*;
import no.noroff.hvz.services.AppUserService;
import no.noroff.hvz.services.GameService;
import no.noroff.hvz.services.PlayerService;
import no.noroff.hvz.services.SquadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Mapper {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private SquadService squadService;

    @Autowired
    private CustomMapper customMapper;

    @Autowired
    private GameService gameService;

    /**
     * Method for mapping missionDTO
     * includes id, name, description, startTime, endTime, faction, coordinates and gameID
     * @param mission the mission
     * @return DTO of the mission
     */
    public MissionDTO toMissionDTO(Mission mission) {
        return new MissionDTO(mission.getId(),mission.getName(),mission.getDescription(),
                mission.getStartTime(), mission.getEndTime(), mission.isHuman(), mission.getLat(), mission.getLng(),mission.getGame().getId());
    }

    /**
     * Method for mapping a mission from a missionDTO
     * @param missionDTO DTO
     * @return Mission
     */
    public Mission toMission(MissionDTOReg missionDTO, long gameId) {
        //Gets the game form the database and uses the information from the DTO to create the mission
        Game game = gameService.getSpecificGame(gameId);
        return new Mission(missionDTO.getName(), missionDTO.isHuman(), missionDTO.getDescription(),
                missionDTO.getStartTime(), missionDTO.getEndTime(), missionDTO.getLat(), missionDTO.getLng(), game);
    }

    public Mission toMissionUpdate(MissionDTOReg missionDTO, long gameId, Long missionID) {
        //Gets the game form the database and uses the information from the DTO to create the mission
        Game game = gameService.getSpecificGame(gameId);
        return new Mission(missionID,missionDTO.getName(), missionDTO.isHuman(), missionDTO.getDescription(),
                missionDTO.getStartTime(), missionDTO.getEndTime(), missionDTO.getLat(), missionDTO.getLng(), game);
    }



    /**
     * Method for mapping DTO for user
     * Includes id, firstname, lastname and players
     * @param user AppUser
     * @return DTO
     */
    public AppUserDTOFull toAppUserDTOFull(AppUser user, boolean isAdmin) {
        return new AppUserDTOFull(user.getOpenId(), user.getFirstName(), user.getLastName(),
                user.getPlayers().stream().map(this::toPlayerDTOFull).collect(Collectors.toSet()), isAdmin
        );
    }

    /**
     * Method for mapping AppUser from DTO
     * @param appUserDTO DTO
     * @return USER
     */
    public AppUser toAppUser(AppUserDTOReg appUserDTO) {
        AppUser appUser = new AppUser();
        appUser.setLastName(appUserDTO.getLastName());
        appUser.setFirstName(appUserDTO.getFirstName());
        return appUser;
    }

    /**
     * Method for mapping from DTO to game for a new game
     * Sets gameState to default Registration
     * @param gameDTO new game contents
     * @return a new game
     */
    public Game toGame(GameDTOReg gameDTO) {
        return new Game(gameDTO.getName(),"Registration", gameDTO.getDescription(), gameDTO.getNw_lat(), gameDTO.getSe_lat(), gameDTO.getNw_long(), gameDTO.getSe_long(), gameDTO.getStartDate(), gameDTO.getEndDate());
    }

    /**
     * Method for mapping from DTO to game for updating
     * @param gameDTOUpdate DTO with updated contents
     * @param id ID of game
     * @return updated game
     */
    public Game toGame(GameDTOUpdate gameDTOUpdate, Long id) {
        //gets the game from database
        Game game = gameService.getSpecificGame(id);
        //Updates contents from DTO
        game.setName(gameDTOUpdate.getName());
        game.setGameState(gameDTOUpdate.getGameState());
        game.setDescription(gameDTOUpdate.getDescription());
        game.setNw_lat(gameDTOUpdate.getNw_lat());
        game.setSe_lat(gameDTOUpdate.getSe_lat());
        game.setNw_long(gameDTOUpdate.getNw_long());
        game.setSe_long(gameDTOUpdate.getSe_long());
        game.setStartDate(gameDTOUpdate.getStartDate());
        game.setEndDate(gameDTOUpdate.getEndDate());
        return game;
    }

    /**
     * Method for mapping from game to gameDTO
     * includes id, name, gameState, description, coordinates and  urls for getting other database objects
     * @param game game to be mapped
     * @return DTO of game
     */
    public GameDTO toGameDTO(Game game) {
        int playerAmount;
        if(game.getPlayers() == null) {
            playerAmount = 0;
        }
        else {
            playerAmount =game.getPlayers().size();
        }
        return new GameDTO(game.getId(), game.getName(),game.getGameState(), game.getDescription(), playerAmount, game.getNw_lat(), game.getSe_lat(),
                game.getNw_long(),game.getSe_long(), game.getStartDate(), game.getEndDate());
    }

    /**
     * Method for mapping from Kill til DTO
     * Includes ID, timeOfDeath, story, coordinates and the names of killer and victim
     * @param kill kill object
     * @return kill DTO
     */
    public KillDTO toKillDTO(Kill kill) {
        String killerName = kill.getKiller().getUser().getFirstName() + " " + kill.getKiller().getUser().getLastName();
        String victimName = kill.getVictim().getUser().getFirstName() + " " + kill.getVictim().getUser().getLastName();
        return new KillDTO(kill.getId(), kill.getTimeOfDeath(), kill.getStory(), kill.getLat(), kill.getLng(),
                killerName, victimName);
    }

    /**
     * Method for mapping from DTO with new content to a new kill
     * @param killDTO DTO with new content
     * @return the new kill
     * @throws InvalidBiteCodeException if the biteCode provided in the dto does not match a player or is already dead
     */
    public Kill regKillDTO(KillDTOReg killDTO, Long gameID) throws InvalidBiteCodeException {
        Kill kill = new Kill();
        //uses customMapper to map new contents onto the kill object
        customMapper.updateKillFromDto(killDTO, kill);
        //gets the killer and victim
        Player killer = playerService.getSpecificPlayer(gameID, killDTO.getKillerID());
        Player victim = playerService.getPlayerByGameAndBiteCode(gameID, killDTO.getBiteCode());
        //checks if no victim was found or the victim was already killed
        if (victim == null) throw new InvalidBiteCodeException("BiteCode did not match any players in this game!");
        if(!victim.isHuman()) throw new InvalidBiteCodeException("The victim is already a zombie");
        kill.setKiller(killer);
        kill.setVictim(victim);
        return kill;
    }

    /**
     * Method for mapping from message to DTO
     * Includes id, message, time, name and the chat the message is for
     * @param message original object
     * @return DTO of message
     */
    public MessageDTO toMessageDTO(Message message) {
        String name = message.getUser().getFirstName() + " " + message.getUser().getLastName();
        return new MessageDTO(message.getId(), message.getMessage(),message.getChatTime(),name , message.isHuman(), message.isGlobal(), message.isFaction());
    }

    /**
     * Method for mapping from DTO with new contents to message
     * @param dto DTO with new content
     * @return new message
     */
    public Message toMessage(MessageDTOreg dto) {
        Message message = new Message();
        message.setMessage(dto.getMessage());
        message.setFaction(dto.isFaction());
        return message;
    }

    /**
     * Method for mapping from DTO with new contents to admin message
     * @param dto DTO with new content
     * @return new message
     */
    public Message toAdminMessage(MessageDTOreg dto) {
        Message message = new Message();
        message.setMessage(dto.getMessage());
        message.setFaction(dto.isFaction());
        message.setHuman(dto.isHuman());
        return message;
    }

    /**
     * Method for mapping from Player to PlayerDTO for normal users
     * Includes name, id, status and biteCode
     * @param player player object
     * @return DTO for normal users
     */
    public PlayerDTOStandard toPlayerDTOStandard(Player player) {
        String name = player.getUser().getFirstName() + " " + player.getUser().getLastName();
        return new PlayerDTOStandard(player.getId(),player.isHuman(), player.getBiteCode(), name, player.getGame().getId());
    }

    /**
     * Method for mapping from Player to PlayerDTO for admins
     * Includes name, id, status, patientZero biteCode, UserDTO and urls for kills and messages
     * @param player player object
     * @return DTO for admin
     */
    public PlayerDTOFull toPlayerDTOFull(Player player) {
        return new PlayerDTOFull(player.getId(),player.isHuman(),player.isPatientZero(), player.getBiteCode(),player.getGame().getId());
    }


    /**
     * Method for mapping from new player DTO to player
     * @param playerDTORegAdmin DTO with new content for when admins create players
     * @return the new player
     */
    public Player regPlayerDTO(PlayerDTORegAdmin playerDTORegAdmin) throws AppUserNotFoundException {
        //gets the user and sets the values in the new player
        AppUser user = appUserService.getSpecificUser(playerDTORegAdmin.getUserID());
        Player player = new Player();
        player.setUser(user);
        player.setHuman(playerDTORegAdmin.isHuman());
        player.setPatientZero(playerDTORegAdmin.isPatientZero());
        return player;
    }

    /**
     * Method for mapping from DTO to squadMember
     * @param dto SquadDTO
     * @return member
     */
    public SquadMember toSquadMember (SquadMemberFromDTO dto, Long gameID) {
        SquadMember member = new SquadMember();
        //gets the player form database and sets the provided rank
        Player player = playerService.getSpecificPlayer(gameID, dto.getPlayerID());
        member.setPlayer(player);
        return member;
    }

    /**
     * Method for mapping from squad to DTO
     * Includes ID, squadName and members
     * @param squad object
     * @return squad DTO
     */
    public SquadDTO toSquadDTO(Squad squad) {
        List<SquadMemberDTO> members = new ArrayList<>();
        int numDead = 0;
        if(squad.getMembers() != null) {
            for (SquadMember member: squad.getMembers()) {
                members.add(toSquadMemberDTO(member));
                if (!member.getPlayer().isHuman()) numDead++;
            }
        }
        // Sort by rank
        if(!members.isEmpty()) {
            members = members.stream()
                    .sorted(Comparator.comparing(SquadMemberDTO::getRank))
                    .collect(Collectors.toList());
        }
        return new SquadDTO(squad.getId(), squad.getName(),members, numDead);
    }

    public SquadMemberDTO toSquadMemberDTO(SquadMember member) {
        return new SquadMemberDTO(toPlayerDTOStandard(member.getPlayer()), member.getRank());
    }

    /**
     * Method for mapping from squadCheckIn to DTO
     * Includes ID, time, coordinates and playerDTO
     * @param squadCheckIn object
     * @return DTO
     */
    public SquadCheckInDTO toSquadCheckInDTO(SquadCheckIn squadCheckIn) {
        PlayerDTO memberDTO = toPlayerDTOStandard(squadCheckIn.getMember().getPlayer());
        return new SquadCheckInDTO(squadCheckIn.getId(), squadCheckIn.getTime(),
                squadCheckIn.getLat(), squadCheckIn.getLng(), memberDTO);
    }

    /**
     * Method for mapping from SquadCheckInDTO with new contents to SquadCheckIn
     * @param squadCheckInDTO DTO with new contents
     * @return SquadCheckIn
     */
    public SquadCheckIn toSquadCheckIn(SquadCheckInDTOReg squadCheckInDTO, Long gameID) {
        Player player = playerService.getSpecificPlayer(gameID,squadCheckInDTO.getPlayerID());
        SquadMember squadMember = squadService.getSquadMemberByPlayer(player);
        return new SquadCheckIn( new Date(), squadCheckInDTO.getLat(), squadCheckInDTO.getLng(), squadMember);
    }

    /**
     * Method to create a squad object from the reg DTO
     * @param dto dto with content for creating the new squad
     * @return the new Squad
     */
    public Squad toSquad(SquadDTOReg dto) {
        Squad s = new Squad();
        s.setName(dto.getName());
        s.setHuman(dto.isHuman());
        return s;
    }
}
