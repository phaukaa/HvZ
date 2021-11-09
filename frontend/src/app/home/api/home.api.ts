import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {baseURL} from "../../../assets/base-url";

@Injectable({
  providedIn: 'root'
})
export class HomeAPI {

  constructor(private readonly http: HttpClient) {
  }

  public getGames(): Observable<any> {
    return this.http.get<any>(baseURL+"api/game");
  }
  public async checkUser() {
    return this.http.get<any>(baseURL + "api/user/log-in");
  }
  public async createUser(user: any) {
    return this.http.post<any>(baseURL + "api/user", user);
  }
  public async createGame(game: any) {
    return this.http.post<any>(baseURL + "api/game", game);
  }
}
