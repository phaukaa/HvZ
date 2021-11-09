package no.noroff.hvz;

import no.noroff.hvz.models.*;
import no.noroff.hvz.repositories.*;
import no.noroff.hvz.services.GameService;
import no.noroff.hvz.services.KillerService;
import no.noroff.hvz.services.PlayerService;
import no.noroff.hvz.services.SquadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
class AppStartupRunner implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(AppStartupRunner.class);

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    KillerRepository killerRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    MissionRepository missionRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    SquadCheckInRepository squadCheckInRepository;

    @Autowired
    SquadMemberRepository squadMemberRepository;

    @Autowired
    SquadRepository squadRepository;

    @Autowired
    PlayerService playerService;

    @Autowired
    KillerService killerService;

    @Autowired
    SquadService squadService;

    @Autowired
    GameService gameService;

    @Override
    public void run(ApplicationArguments args) {
        logger.info("Your application started with option names : {}", args.getOptionNames());

        AppUser user1 = new AppUser();
        AppUser user2 = new AppUser();
        AppUser user3 = new AppUser();

//        AppUser patrick = new AppUser();
        AppUser fredrik = new AppUser();

        Game game1 = new Game();
        Game game2 = new Game();

        Kill kill1 = new Kill();
        Kill kill2 = new Kill();
        Kill kill3 = new Kill();

        Message message1 = new Message();
        Message message2 = new Message();
        Message message3 = new Message();
        Message message4 = new Message();
        Message message5 = new Message();
        Message message6 = new Message();
        Message message7 = new Message();
        Message message8 = new Message();

        Mission mission1 = new Mission();
        Mission mission2 = new Mission();
        Mission mission3 = new Mission();

        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();
        Player player4 = new Player();
        Player player5 = new Player();

//        Player playerPatrick = new Player();
        Player playerFredrik = new Player();

        Squad squad1 = new Squad();

        SquadCheckIn sci1 = new SquadCheckIn();
        SquadCheckIn sci2 = new SquadCheckIn();

        SquadMember member1 = new SquadMember();
        SquadMember member2 = new SquadMember();
//        SquadMember memberPatrick = new SquadMember();
        SquadMember memberFredrik = new SquadMember();

        game1.setGameState("Registration");
        game1.setName("Game numero uno");
        game1.setDescription("An open game, which is numero uno");
        game1.setNw_lat("59.934319");
        game1.setSe_long("10.695490");
        game1.setSe_lat("59.884573");
        game1.setNw_long("10.833259");

        game2.setGameState("Complete");
        game2.setName("Game numero dos");
        game2.setDescription("A closed game, which is numero dos");
        game2.setNw_lat("59.413878");
        game2.setSe_long("17.807955");
        game2.setSe_lat("59.261201");
        game2.setNw_long("18.203022");

        if (gameRepository.count() == 0) {
            gameRepository.save(game1);
            gameRepository.save(game2);
        }

        mission1.setGame(game1);
        mission1.setHuman(true);
        mission1.setName("Going to find Narnia");
        mission1.setDescription("A human mission to find Narnia");
        mission1.setStartTime(new Date());
        mission1.setEndTime(new Date());
        mission1.setLat("59.909427");
        mission1.setLng("10.761406");

        mission2.setGame(game1);
        mission2.setHuman(false);
        mission2.setName("Going to melt the ring");
        mission2.setDescription("Some zombies need to find a volcano to meld the ring");
        mission2.setStartTime(new Date());
        mission2.setEndTime(new Date());
        mission2.setLat("59.925173");
        mission2.setLng("10.740978");

        mission3.setGame(game2);
        mission3.setHuman(true);
        mission3.setName("Kill all the zombies");
        mission3.setDescription("Another game, another mission");
        mission3.setStartTime(new Date());
        mission3.setEndTime(new Date());
        mission3.setLat("59.329687");
        mission3.setLng("18.067781");

        if (missionRepository.count() == 0) {
            mission1 = missionRepository.save(mission1);
            mission1 = missionRepository.save(mission2);
            mission1 = missionRepository.save(mission3);
        }

        user1.setFirstName("Per");
        user1.setLastName("Persen");
        user1.setOpenId("openId1");

        user2.setFirstName("Hans");
        user2.setLastName("Hansen");
        user2.setOpenId("openId2");

        user3.setFirstName("John");
        user3.setLastName("Johnsen");
        user3.setOpenId("openId3");

//        patrick.setFirstName("Patrick");
//        patrick.setLastName("Haukaa");
//        patrick.setOpenId("google-oauth2|112037027886754103927");
        fredrik.setFirstName("Fredrik");
        fredrik.setLastName("Rikheim");
        fredrik.setOpenId("google-oauth2|108443371955945011845");

        if (appUserRepository.count() == 0) {
            user1 = appUserRepository.save(user1);
            user2 = appUserRepository.save(user2);
            user3 = appUserRepository.save(user3);
//            patrick = appUserRepository.save(patrick);
            fredrik = appUserRepository.save(fredrik);
        }

        player1.setHuman(true);
        player1.setUser(user1);
        player1.setPatientZero(false);

        player2.setHuman(false);
        player2.setUser(user2);
        player2.setPatientZero(true);

        player3.setHuman(true);
        player3.setUser(user3);
        player3.setPatientZero(false);

        player4.setHuman(true);
        player4.setUser(user2);
        player4.setPatientZero(false);

        player5.setHuman(false);
        player5.setUser(user3);
        player5.setPatientZero(false);

//        playerPatrick.setHuman(true);
//        playerPatrick.setUser(patrick);
//        playerPatrick.setPatientZero(false);
        playerFredrik.setHuman(true);
        playerFredrik.setUser(fredrik);
        playerFredrik.setPatientZero(false);

        playerService.createNewPlayer(1L, player1);
        playerService.createNewPlayer(1L, player2);
        playerService.createNewPlayer(1L, player3);
        playerService.createNewPlayer(2L, player4);
        playerService.createNewPlayer(2L, player5);
//        playerService.createNewPlayer(1L, playerPatrick);
        playerService.createNewPlayer(1L, playerFredrik);


        kill1.setKiller(player2);
        kill1.setVictim(player3);
        kill1.setTimeOfDeath(new Date());
        kill1.setStory("He killed him brutally");
        kill1.setLat("59.912941");
        kill1.setLng("10.761066");

        killerService.createNewKill(1L, kill1);

        squad1.setHuman(true);
        squad1.setName("The cool gang in game 1");

        squadService.createNewSquad(1L, squad1);

        message1.setMessage("Hello World!");
        message1.setGlobal(true);
        message1.setFaction(false);

        message2.setMessage("Hello global chat!");
        message2.setGlobal(true);
        message2.setFaction(false);

        message3.setMessage("Hello faction!");
        message3.setGlobal(false);
        message3.setFaction(true);

        message4.setMessage("This faction chat is nice!!");
        message4.setGlobal(false);
        message4.setFaction(true);

        message5.setMessage("Hello Game 2!");
        message5.setGlobal(true);
        message5.setFaction(false);

        message6.setMessage("Hello Player 4! Greetings from number 5");
        message6.setGlobal(true);
        message6.setFaction(false);

        message7.setMessage("Hello Squad");
        message7.setFaction(false);

        message8.setMessage("Hello Squad indeed");
        message8.setFaction(false);

        gameService.createNewChat(1L, message1, user1);
        gameService.createNewChat(1L, message2, user2);
        gameService.createNewChat(1L, message3, user1);
        gameService.createNewChat(1L, message4, user3);
        gameService.createNewChat(2L, message5, user2);
        gameService.createNewChat(2L, message6, user3);

        squadService.createSquadChat(1L, 1L, user1, message7);
        squadService.createSquadChat(1L, 1L, user2, message8);

        member1.setRank(1);
        member1.setPlayer(player1);

        member2.setRank(2);
        member2.setPlayer(player2);

//        memberPatrick.setRank(3);
//        memberPatrick.setPlayer(playerPatrick);

        memberFredrik.setRank(4);
        memberFredrik.setPlayer(playerFredrik);

        squadService.joinSquad(1L, 1L, member1);
        squadService.joinSquad(1L, 1L, member2);
//        squadService.joinSquad(1L, 1L, memberPatrick);
        squadService.joinSquad(1L, 1L, memberFredrik);

        sci1.setMember(member1);
        sci1.setTime(new Date());
        sci1.setLat("59.913931");
        sci1.setLng("10.740858");

        sci2.setMember(member2);
        sci2.setTime(new Date());
        sci2.setLat("59.917659");
        sci2.setLng("10.764178");

        squadService.createSquadCheckIn(1L, 1L, sci1);
        squadService.createSquadCheckIn(1L, 1L, sci2);
    }
}