import { Component, OnInit } from '@angular/core';
import {GameInfoAPI} from "../api/game-info.api";
import {ActivatedRoute, Router} from "@angular/router";
import {PlayerInfo} from "../../models/input/player-info.model";
import {GameInfo} from "../../models/input/game-info.model";
import {Mission} from "../../models/input/mission.model";
import {Kill} from "../../models/input/kill.model";
import {Message} from "../../models/input/message.model";
import {WebSocketAPI} from "../api/WebSocketApi.api";
import LatLng = google.maps.LatLng;
import {SquadCheckIn} from "../../models/input/squad-check-in.model";

@Component({
  selector: 'app-game-info-page',
  templateUrl: './game-info.page.html',
  styleUrls: ['./game-info.page.css']
})
export class GameInfoPage implements OnInit {
  //Holder for the game, initialized in ngOnInit
  private gameInfo: GameInfo = {
    gameID: 0,
    //All the variables are initialized in safe states or error states.
    playerID: null,
    player_is_human: true,
    name: "Not a player in this game",
    state: "ERROR: No game state found",
    description: "",
    bite_code: "Not a player in this game",
    squad_info: null,
    map_info: {nw_lat: null, nw_long: null, se_lat: null, se_long: null},
    messages: [],
    kills: [],
    missions: [],
    squadCheckIns: []
  };
  public allSquads = null;

  private messagesURL!: string;

  private selectedChat = "Global";
  private prevMessageSent: String | undefined;

  private webSocketAPI!: WebSocketAPI;

  locationRequested: boolean = false;

  playerLocation: LatLng | null = null;

  public isMobile: boolean;

  constructor(private readonly gameInfoAPI: GameInfoAPI, private route: ActivatedRoute, private readonly router: Router) {
    this.isMobile = window.innerWidth < 768;
  }

  ngOnInit(): void {
    //Finding gameID and playerID from the optional params
    this.gameInfo.gameID = parseInt(this.route.snapshot.paramMap.get("id")!);
    const playerIDTemp = this.route.snapshot.paramMap.get("playerId");
    if (playerIDTemp != null) this.gameInfo.playerID = parseInt(playerIDTemp);

    //Getting information about the specific game
    this.gameInfoAPI.getGameById(this.gameInfo.gameID)
      .then((response) => {
        response.subscribe((game) => {
            this.gameInfo.name = game.name;
            this.gameInfo.state = game.gameState;
          this.gameInfo.description = game.description;
          try {
            this.gameInfo.map_info = {
              nw_lat: parseFloat(game.nw_lat),
              se_lat: parseFloat(game.se_lat),
              nw_long: parseFloat(game.nw_long),
              se_long: parseFloat(game.se_long)
            };
          } catch (e) {}
            this.messagesURL = game.messages;
          });
      });
    if (this.gameInfo.playerID != null) { //Only runs if the player has already joined the game.
      //Getting information about the specific player.
      this.gameInfoAPI.getCurrentPlayerInfo(this.gameInfo.gameID, this.gameInfo.playerID)
        .then((response) => {
          response.subscribe((player) => {
            this.gameInfo.bite_code = player.biteCode;
            this.gameInfo.player_is_human = player.human;
          });
        });
      this.updateSquad(); //Also updates the squad check-ins.
      //Getting information about map markers.
      this.updateMissions();
      this.updateMessagesGlobal();
    }
    this.updateKills();
    this.getAllSquads();

    // Connecting the WebSocket
    this.webSocketAPI = new WebSocketAPI(this);
    this.connect();
  }

  getAllSquads() {
    this.gameInfoAPI.getAllSquads(this.gameInfo.gameID)
      .then(res => {
        res.subscribe(squads => this.allSquads = squads)
      });
  }

  updateMissions() {
    const tempMissions: Mission[] = [];
    this.gameInfoAPI.getMissionsByGame(this.gameInfo.gameID)
      .then((response) => {
        response.subscribe((missions) => {
          for (let mission of missions) {
            tempMissions.push({
              name: mission.name.toString(),
              id: mission.id,
              description: mission.description.toString(),
              endTime: mission.endTime.toString(),
              startTime: mission.startTime.toString(),
              lat: parseFloat(mission.lat),
              lng: parseFloat(mission.lng),
              human: mission.isHuman,
              gameId: this.game.gameID
            });
          }
          this.gameInfo.missions = tempMissions;
        });
      });
  }

  updateCheckIns() {
    const tempCheckIns: SquadCheckIn[] = [];
    if (this.gameInfo.squad_info != null) {
      this.gameInfoAPI.getSquadCheckIns(this.gameInfo.gameID, this.gameInfo.squad_info.id)
        .then((response) => {
          response.subscribe((checkIns) => {
            for (let checkIn of checkIns) {
              tempCheckIns.push({
                id: checkIn.id,
                lat: parseFloat(checkIn.lat),
                lng: parseFloat(checkIn.lng),
                time: checkIn.time.toString(),
                member:  {
                  name: checkIn.member.name.toString(),
                  human: checkIn.member.human,
                },
              });
            }
            this.gameInfo.squadCheckIns = tempCheckIns;
          });
        });
    }
  }

  updateKills() {
    const tempKills: Kill[] = [];
    this.gameInfoAPI.getKillsByGame(this.gameInfo.gameID)
      .then((response) => {
        response.subscribe((kills) => {
          for (let kill of kills) {
            tempKills.push({
              id: kill.id,
              killerName: kill.killerName.toString(),
              lat: parseFloat(kill.lat),
              lng: parseFloat(kill.lng),
              story: kill.story,
              timeOfDeath: kill.timeOfDeath.toString(),
              victimName: kill.victimName.toString()
            });
          }
          this.gameInfo.kills = tempKills;
        });
      });
  }

  updateMessagesGlobal() {
    const tempMessages: Message[] = [];
    this.gameInfoAPI.getGameChat(this.gameInfo.gameID)
      .then((response) => {
        response.subscribe((messages) => {
          for (let message of messages) {
            tempMessages.push({
              id:message.id,
              global: message.global,
              human: message.human,
              sender: message.playerName,
              time: message.messageTime,
              content: message.message
            });
          }
          this.gameInfo.messages = tempMessages;
        })
      });
  }

  updateSquad() {
    this.gameInfoAPI.getCurrentPlayerSquad(this.gameInfo.gameID, this.gameInfo.playerID!)
      .then((response) => {
        response.subscribe((squads) => {
          const members: PlayerInfo[] = [];
          for (let member of squads[0].members) {
            members.push({name: member.player.name, state: member.player.human, rank: member.rank});
          }
          this.gameInfo.squad_info = {name: squads[0].name, members: members, id: squads[0].id, numDead: squads[0].numDead};
          if (this.gameInfo.player_is_human) this.updateCheckIns();
        });
      });
  }

  getLocation(): void {
    this.locationRequested = true;
  }

  locationFound(location: LatLng): void {
    this.locationRequested = false;
    this.playerLocation = location;
  }

  //Creates a new player for the currently logged in user.
  registerUser(): void {
    this.gameInfoAPI.createPlayer(this.gameInfo.gameID)
      .then(res => res.subscribe(
        data => { //If the response is ok, there is a player that can be used.
          this.gameInfo.playerID = data.id
          return this.router.navigate(["game/"+this.gameInfo.gameID+"/player/"+this.gameInfo.playerID]);
        }
      ));
  }

  // Connect to the WebSocket
  connect(){
    this.webSocketAPI._connect();
  }

  // Disconnect from the WebSocket
  disconnect(){
    this.webSocketAPI._disconnect();
  }

  // Send a message to the backend
  sendMessage(){
    this.webSocketAPI._send(this.gameInfo.gameID);
  }

  // Handle message from backend (notification)
  // Reloads the chat
  handleMessage(){
    if (this.selectedChat == "Global") this.loadGlobalChat();
    else if (this.selectedChat == "Faction") this.loadFactionChat();
    else if (this.selectedChat == "Squad") this.loadSquadChat();
  }

  get game(): GameInfo {
    return this.gameInfo;
  }

  // Loads the global chat into the chat window
  public loadGlobalChat() {
    this.selectedChat = "Global";
    this.updateMessagesGlobal();
  }

  // Loads the faction chat into the chat window
  loadFactionChat() {
    this.selectedChat = "Faction";
    const tempMessages: Message[] = [];
    this.gameInfoAPI.getFactionChat(this.gameInfo.gameID, this.gameInfo.player_is_human)
      .then((response) => {
        response.subscribe((messages) => {
          for (let message of messages) {
            tempMessages.push({
              id:message.id,
              global: message.global,
              human: message.human,
              sender: message.playerName,
              time: message.messageTime,
              content: message.message
            });
          }
          this.gameInfo.messages = tempMessages;
        })
      });
  }

  // Loads the squad chat into the chat window
  loadSquadChat() {
    this.selectedChat = "Squad";
    const tempMessages: Message[] = [];
    this.gameInfoAPI.getSquadChat(this.gameInfo.gameID, this.gameInfo.squad_info!.id)
      .then((response) => {
        response.subscribe((messages) => {
          for (let message of messages) {
            tempMessages.push({
              id:message.id,
              global: message.global,
              human: message.human,
              sender: message.playerName,
              time: message.messageTime,
              content: message.message
            });
          }
          this.gameInfo.messages = tempMessages;
        })
      });
  }

  // Method for sending a new chat message to the selected chat
  sendChatMessage(message: String) {
    if (this.selectedChat == "Global") {
      this.gameInfoAPI.sendGlobalChat(this.gameInfo.gameID, message)
        .then((res) => {
          res.subscribe(msg => {
            this.prevMessageSent = msg.message;
            // Reload chat when a message is sent
            this.loadGlobalChat();
          })
        });
    } else if (this.selectedChat == "Faction") {
      this.gameInfoAPI.sendFactionChat(this.gameInfo.gameID, message)
        .then((res) => {
          res.subscribe(msg => {
            this.prevMessageSent = msg.message;
            // Reload chat when a message is sent
            this.loadFactionChat();
          })
        });
    } else {
      this.gameInfoAPI.sendSquadChat(this.gameInfo.gameID, this.gameInfo.squad_info!.id, message)
        .then((res) => {
          res.subscribe(msg => {
            this.prevMessageSent = msg.message;
            // Reload chat when a message is sent
            this.loadSquadChat();
          })
        });
    }
    this.sendMessage();
  }

  joinSquad(squadID: number) {
    if (this.gameInfo.playerID != null) {
      this.gameInfoAPI.joinSquad(this.gameInfo.gameID, squadID, this.gameInfo.playerID)
        .then(res => {
          res.subscribe(() => {
            this.updateSquad();
          })
        });
    }
  }

  createSquad(squadName: string) {
    this.gameInfoAPI.createSquad(this.gameInfo.gameID, squadName, this.gameInfo.player_is_human)
      .then(res => {
        res.subscribe(() => {
          this.updateSquad();
        })
      })
  }

  leaveSquad() {
    if (this.gameInfo.squad_info) {
      this.gameInfoAPI.leaveSquad(this.gameInfo.gameID, this.gameInfo.squad_info!.id)
        .then(res => {
          res.subscribe()
        });
      this.gameInfo.squad_info = null;
      this.gameInfo.squadCheckIns = [];
    }
  }
}
