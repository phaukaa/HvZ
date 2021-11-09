import {Component, Input, OnChanges} from '@angular/core';
import {Router} from "@angular/router";
import {UserPlayer} from "../../../models/input/user-player.model";
import {AuthService} from "@auth0/auth0-angular";

@Component({
  selector: 'app-active-game',
  templateUrl: './active-game.component.html',
  styleUrls: ['./active-game.component.css']
})
export class ActiveGameComponent implements OnChanges {

  @Input()
  public gameName: String = "";
  @Input()
  public startTime!: String | null;
  @Input()
  public endTime!: String | null;
  @Input()
  public gameStatus: String = "";
  @Input()
  public gameId: number = 0;
  @Input()
  public activePlayer: UserPlayer | null = null;
  @Input()
  public isMobile!: boolean;
  @Input()
  public isAdmin!: boolean;
  @Input()
  public playerAmount!: number;
  @Input()
  public playerID!: number | null;

  constructor(private readonly router: Router, public auth: AuthService) { }

  ngOnChanges() {
  }

  toGameInfo(gameId: number, playerID: number | null): Promise<boolean> | void {
    if (playerID == null) {
      return this.router.navigate(["game/"+gameId]);
    }
    else {
      return this.router.navigate(["game/"+gameId+"/player/"+playerID]);
    }
  }
  toGameInfoAdmin(gameId: number): Promise<boolean> {
    return this.router.navigate(["game/"+gameId+"/admin"]);
  }

}
