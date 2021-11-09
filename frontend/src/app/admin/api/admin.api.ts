import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {baseURL} from "../../../assets/base-url";
import {Mission} from "../../models/input/mission.model";
import {KillOutput} from "../../models/output/kill-output.model";
import {GameOutput} from "../../models/output/game-output.model";
import {PlayerInfoFull} from "../../models/input/player-info-full.model";
import {PlayerAdminOutput} from "../../models/output/player-admin-output.model";

@Injectable({
  providedIn: 'root'
})
export class AdminAPI {

  constructor(private readonly http: HttpClient) {
  }

  public async getGameById(gameID: number): Promise<Observable<any>> {
    return await this.http.get<any>(baseURL+"api/game/"+gameID);
  }
  public async getMissionsByGame(gameID: number): Promise<Observable<any>> {
    return await this.http.get<any>(baseURL+"api/game/"+gameID+"/mission");
  }
  public async getKillsByGame(gameID: number): Promise<Observable<any>> {
    return await this.http.get<any>(baseURL+"api/game/"+gameID+"/kill");
  }
  public async getMessagesByPlayer(gameID: number, playerID: number): Promise<Observable<any>> {
    return await this.http.get<any>(baseURL+"api/game/"+gameID+"/player/"+playerID+"/message");
  }
  public async createMission(gameID: number, mission: Mission): Promise<Observable<any>> {
    return await this.http.post<any>(baseURL+"api/game/"+gameID+"/mission/", mission);
  }
  public async updateMission(gameID: number, missionID: number, mission: Mission): Promise<Observable<any>> {
    return await this.http.put<any>(baseURL+"api/game/"+gameID+"/mission/"+missionID, mission);
  }
  public async deleteMission(gameID: number, missionID: number): Promise<Observable<any>> {
    return await this.http.delete<any>(baseURL+"api/game/"+gameID+"/mission/"+missionID);
  }
  public async createKill(gameID: number, kill: KillOutput): Promise<Observable<any>> {
    return await this.http.post<any>(baseURL+"api/game/"+gameID+"/kill/", kill);
  }
  public async updateKill(gameID: number, killID: number, kill: KillOutput): Promise<Observable<any>> {
    return await this.http.put<any>(baseURL+"api/game/"+gameID+"/kill/"+killID, kill);
  }
  public async deleteKill(gameID: number, killID: number): Promise<Observable<any>> {
    return await this.http.delete<any>(baseURL+"api/game/"+gameID+"/kill/"+killID);
  }
  public async updateGame(gameID: number, game: GameOutput): Promise<Observable<any>> {
    return await this.http.put<any>(baseURL+"api/game/"+gameID, game);
  }
  public async updatePlayer(gameID: number, playerID: number, player: PlayerInfoFull): Promise<Observable<any>> {
    return await this.http.put<any>(baseURL+"api/game/"+gameID+"/player/"+playerID, player)
  }
  public async createPlayer(gameID: number, player: PlayerAdminOutput): Promise<Observable<any>> {
    return await this.http.post<any>(baseURL+"api/game/"+gameID+"/player", player);
  }
  public async deletePlayer(gameID: number,  playerID: number): Promise<Observable<any>> {
    return await this.http.delete(baseURL+"api/game/"+gameID+"/player/"+playerID);
  }
  public async getAllUsers(): Promise<Observable<any>> {
    return await this.http.get<any>(baseURL+"api/user");
  }
  public async getGameChat(gameID: number): Promise<Observable<any>> {
    return await this.http.get<any>(baseURL + "api/game/" + gameID + "/chat");
  }
  public async getFactionChat(gameID: number, isHuman: boolean) {
    let header = new HttpHeaders({"human": JSON.stringify(isHuman)});
    return await this.http.get<any>(baseURL+"api/game/"+gameID+"/chat", {headers: header})
  }
  public async sendGlobalChat(gameID: number, message: String): Promise<Observable<any>> {
    return await this.http.post(baseURL+"api/game/"+gameID+"/chat", {"message":message, "faction":false});
  }
  public async sendHumanChat(gameID: number, message: String): Promise<Observable<any>> {
    return await this.http.post(baseURL+"api/game/"+gameID+"/chat", {"message":message, "faction":true, "human":true});
  }
  public async sendZombieChat(gameID: number, message: String): Promise<Observable<any>> {
    return await this.http.post(baseURL+"api/game/"+gameID+"/chat", {"message":message, "faction":true, "human":false});
  }
  public async getAllSquads(gameID: number) {
    return await this.http.get<any>(baseURL + "api/game/"+gameID+"/squad");
  }
  public async getSquadChat(gameID: number, squadID: number) {
    return await this.http.get<any>(baseURL+"api/game/"+gameID+"/squad/"+squadID+"/chat");
  }
  public async getPlayerChat(gameID: number, playerID: number) {
    let header = new HttpHeaders({"playerID": JSON.stringify(playerID)});
    return await this.http.get<any>(baseURL+"api/game/"+gameID+"/chat", {headers: header});
  }
  public async sendSquadChat(gameID: number, squadID: number, message: String): Promise<Observable<any>> {
    return await this.http.post(baseURL+"api/game/"+gameID+"/squad/"+squadID+"/chat", {"message":message, "faction":false});
  }
}
