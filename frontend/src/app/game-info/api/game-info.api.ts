import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {baseURL} from "../../../assets/base-url";
import {KillOutput} from "../../models/output/kill-output.model";
import {SquadCheckInOutput} from "../../models/output/squad-check-in-output";

@Injectable({
  providedIn: 'root'
})
export class GameInfoAPI {

  constructor(private readonly http: HttpClient) {
  }

  public async getGameById(gameID: number): Promise<Observable<any>> {
    return await this.http.get<any>(baseURL+"api/game/"+gameID);
  }
  public async getCurrentPlayerInfo(gameID: number, playerID: number): Promise<Observable<any>> {
    return await this.http.get<any>(baseURL + "api/game/" + gameID + "/player/" + playerID);
  }
  public async getCurrentPlayerSquad(gameID: number, playerID: number): Promise<Observable<any>> {
    return await this.http.get<any>(baseURL+"api/game/"+gameID+"/squad?playerId=" + playerID);
  }
  public async getMissionsByGame(gameID: number): Promise<Observable<any>> {
    return await this.http.get<any>(baseURL+"api/game/"+gameID+"/mission");
  }
  public async getKillsByGame(gameID: number): Promise<Observable<any>> {
    return await this.http.get<any>(baseURL+"api/game/"+gameID+"/kill");
  }
  public async getGameChat(gameID: number): Promise<Observable<any>> {
    return await this.http.get<any>(baseURL+"api/game/"+gameID+"/chat");
  }
  public async getFactionChat(gameID: number, isHuman: boolean) {
    let header = new HttpHeaders({"human": JSON.stringify(isHuman)});
    return await this.http.get<any>(baseURL+"api/game/"+gameID+"/chat", {headers: header})
  }
  public async getSquadChat(gameID: number, squadID: number) {
    return await this.http.get<any>(baseURL+"api/game/"+gameID+"/squad/"+squadID+"/chat");
  }
  public async sendGlobalChat(gameID: number, message: String): Promise<Observable<any>> {
    return await this.http.post(baseURL+"api/game/"+gameID+"/chat", {"message":message, "faction":false});
  }
  public async sendFactionChat(gameID: number, message: String): Promise<Observable<any>> {
    return await this.http.post(baseURL+"api/game/"+gameID+"/chat", {"message":message, "faction":true});
  }
  public async sendSquadChat(gameID: number, squadID: number, message: String): Promise<Observable<any>> {
    return await this.http.post(baseURL+"api/game/"+gameID+"/squad/"+squadID+"/chat", {"message":message, "faction":false});
  }
  public async getAllSquads(gameID: number) {
    return await this.http.get<any>(baseURL + "api/game/"+gameID+"/squad");
  }
  public async joinSquad(gameID: number, squadID: number, playerID: number) {
    return await this.http.post(baseURL+"api/game/"+gameID+"/squad/"+squadID+"/join", {playerID: playerID})
  }
  public async createSquad(gameID: number, squadName: string, human: boolean) {
    return this.http.post(baseURL+"api/game/"+gameID+"/squad", {name: squadName, human: human})
  }
  public async createKill(gameID: number, kill: KillOutput): Promise<Observable<any>> {
    return await this.http.post<any>(baseURL + "api/game/" + gameID + "/kill/", kill);
  }
  public async leaveSquad(gameID: number, squadID: number) {
    return await this.http.delete(baseURL + "api/game/" + gameID + "/squad/" + squadID + "/leave");
  }
  public async getSquadCheckIns(gameID: number, squadID: number) {
    return await this.http.get<any>(baseURL + "api/game/"+gameID+"/squad/"+squadID+"/check-in");
  }
  public async squadCheckIn(gameID: number, squadID: number, checkIn: SquadCheckInOutput) {
    return await this.http.post<any>(baseURL + "api/game/"+gameID+"/squad/"+squadID+"/check-in", checkIn);
  }
  public async createPlayer(gameID: number) {
    return this.http.post<any>(baseURL+"api/game/"+gameID+"/player", null);
  }
}
