import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Mission} from "../../models/input/mission.model";
import {Kill} from "../../models/input/kill.model";
import {AdminAPI} from "../api/admin.api";
import {GameInfoAdmin} from "../../models/input/game-info-admin.model";
import {PlayerInfoFull} from "../../models/input/player-info-full.model";
import {GameOutput} from "../../models/output/game-output.model";
import {Message} from "../../models/input/message.model";
import {WebSocketAPI} from "../../game-info/api/WebSocketApi.api";

@Component({
  selector: 'app-admin.page',
  templateUrl: './admin.page.html',
  styleUrls: ['./admin.page.css']
})
export class AdminPage implements OnInit {
  //Holder for the game, initialized in ngOnInit
  //All the variables are initialized in safe states or error states.
  private gameInfo: GameInfoAdmin = {
    id: 0,
    name: "ERROR: No game name found",
    state: "ERROR: No game state found",
    description: "",
    squad_info: null,
    map_info: {nw_lat: null, nw_long: null, se_lat: null, se_long: null},
    kills: [],
    missions: [],
    players: [],
    squadCheckIns: []
  };

  //Used for the selects in the map component.
  private humanBiteCodesArray: {name: string, biteCode: string}[] = [];
  private zombieIDsArray: {name: string, id: number}[] = [];

  private selectedChat: String = "Global";
  public loadedMessages: Message[] = [];
  private webSocketAPI!: WebSocketAPI;

  public squads = null;
  public selectedSquad: number = 0;

  public isMobile: boolean;

  constructor(private readonly adminAPI: AdminAPI, private route: ActivatedRoute) {
    this.isMobile = window.innerWidth < 768;
  }

  ngOnInit(): void {
    //Finding gameID from the optional params
    this.gameInfo.id = parseInt(this.route.snapshot.paramMap.get("id")!);
    //Getting information about the specific game
    this.updateGame();
    //Getting information about map markers.
    this.updateMissions();
    this.updateKills();
    this.updatePlayers();
    this.loadGlobalChat();
    // Connecting the WebSocket
    this.webSocketAPI = new WebSocketAPI(this);
    this.connect();
    this.getAllSquads();
  }

  //Saves the changes made by the admin. Runs when the Save button is clicked.
  saveTitle(): void {
    const updateGame: GameOutput = {
      description: this.gameInfo.description,
      gameState: this.gameInfo.state,
      name: this.gameInfo.name,
      nw_lat: this.gameInfo.map_info.nw_lat,
      nw_long: this.gameInfo.map_info.nw_long,
      se_lat: this.gameInfo.map_info.se_lat,
      se_long: this.gameInfo.map_info.se_long
    }
    this.adminAPI.updateGame(this.gameInfo.id, updateGame)
      .then(res => res.subscribe(
        () => this.updateGame()
      ));
  }

  /* ************************************************************
   * Methods that update the objects referenced in their names. *
   ************************************************************ */

  updateMissions(): void {
    const tempMissions: Mission[] = [];
    this.adminAPI.getMissionsByGame(this.gameInfo.id)
      .then((response) => {
        response.subscribe((missions) => {
          for (let mission of missions) {
            tempMissions.push({
              name: mission.name.toString(),
              id: mission.id,
              description: mission.description.toString(),
              endTime: mission.endTime,
              startTime: mission.startTime,
              lat: parseFloat(mission.lat),
              lng: parseFloat(mission.lng),
              human: mission.human,
              gameId: this.game.id
            });
          }
          this.gameInfo.missions = tempMissions;
        });
      });
  }

  updateKills(): void {
    const tempKills: Kill[] = [];
    this.adminAPI.getKillsByGame(this.gameInfo.id)
      .then((response) => {
        response.subscribe((kills) => {
          for (let kill of kills) {
            tempKills.push({
              id: kill.id,
              killerName: kill.killerName.toString(),
              lat: parseFloat(kill.lat),
              lng: parseFloat(kill.lng),
              story: kill.story.toString(),
              timeOfDeath: kill.timeOfDeath.toString(),
              victimName: kill.victimName.toString()
            });
          }
          this.gameInfo.kills = tempKills;
        });
      });
  }

  updateGame(): void {
    this.adminAPI.getGameById(this.gameInfo.id)
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
        });
      });
  }

  updatePlayers(): void {
    const tempPlayers: PlayerInfoFull[] = [];
    this.adminAPI.getAllUsers()
      .then((response) => {
        response.subscribe((users) => {
          for (let user of users) {
            for (let player of user.players) {
              if (player.gameID == this.gameInfo.id) {
                if (player.human) { //Used in the map component for creating and updating kills.
                  this.humanBiteCodesArray.push({name: user.firstName + " " + user.lastName, biteCode: player.biteCode});
                }
                else {
                  this.zombieIDsArray.push({name: user.firstName + " " + user.lastName, id: player.id});
                }
                tempPlayers.push({ //Used in the players component as a list
                  id: player.id,
                  name: user.firstName + " " + user.lastName,
                  human: player.human,
                  biteCode: player.biteCode,
                  kills: player.kills,
                  messages: player.messages
                });
              }
              this.gameInfo.players = tempPlayers;
            }
          }
        });
      });
  }

  //Getters
  get humanBiteCodes(): {name: string, biteCode: string}[] {
    return this.humanBiteCodesArray;
  }
  get zombieIDs(): {name: string, id: number}[] {
    return this.zombieIDsArray;
  }
  get game(): GameInfoAdmin {
    return this.gameInfo;
  }

  /* *****************
   * Chat functions. *
   ***************** */
  loadGlobalChat() {
    this.selectedChat = "Global";
    const tempMessages: Message[] = [];
    this.adminAPI.getGameChat(this.gameInfo.id)
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
          this.loadedMessages = tempMessages;
        })
      });
  }

  loadFactionChat(human: boolean) {
    const tempMessages: Message[] = [];
    this.adminAPI.getFactionChat(this.gameInfo.id, human)
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
          this.loadedMessages = tempMessages;
        })
      });
  }

  loadHumanChat() {
    this.selectedChat = "Human";
    this.loadFactionChat(true);
  }

  loadZombieChat() {
    this.selectedChat = "Zombie";
    this.loadFactionChat(false);
  }

  loadSquadChat(squadID: number) {
    this.selectedChat = "Squad";
    this.selectedSquad = squadID;
    const tempMessages: Message[] = [];
    this.adminAPI.getSquadChat(this.gameInfo.id, squadID)
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
          this.loadedMessages = tempMessages;
        })
      });
  }

  loadPlayerChat(playerID: number) {
    this.selectedChat = "Player";
    const tempMessages: Message[] = [];
    this.adminAPI.getPlayerChat(this.gameInfo.id, playerID)
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
          this.loadedMessages = tempMessages;
        })
      });
  }

  sendChatMessage(message: String) {
    if (this.selectedChat == "Global") {
      this.adminAPI.sendGlobalChat(this.gameInfo.id, message)
        .then((res) => {
          res.subscribe(() => {
            // Reload chat when a message is sent
            this.loadGlobalChat();
          })
        });
    } else if (this.selectedChat == "Human") {
      this.adminAPI.sendHumanChat(this.gameInfo.id, message)
        .then((res) => {
          res.subscribe(() => {
            // Reload chat when a message is sent
            this.loadHumanChat();
          })
        });
    } else if (this.selectedChat == "Zombie") {
      this.adminAPI.sendZombieChat(this.gameInfo.id, message)
        .then((res) => {
          res.subscribe(() => {
            // Reload chat when a message is sent
            this.loadZombieChat();
          })
        });
    } else if (this.selectedChat == "Squad") {
      this.adminAPI.sendSquadChat(this.gameInfo.id, this.selectedSquad, message)
        .then(res => {
          res.subscribe(() => {
            this.loadSquadChat(this.selectedSquad)
          })
        });
    }
    this.sendMessage();
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
    this.webSocketAPI._send(this.gameInfo.id);
  }

  //Not an unused method even if IntelliJ says so
  handleMessage(){
    if (this.selectedChat == "Global") this.loadGlobalChat();
    else if (this.selectedChat == "Human") this.loadHumanChat();
    else if (this.selectedChat == "Zombie") this.loadZombieChat();
  }

  getAllSquads() {
    this.adminAPI.getAllSquads(this.gameInfo.id)
      .then(res => {
          res.subscribe(squads => {
            this.squads = squads
          })
      });
  }
}
