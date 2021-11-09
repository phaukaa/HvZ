import { Component, OnInit } from '@angular/core';
import {ActiveGame} from "../../models/input/active-game.model";
import {HomeAPI} from "../api/home.api";
import {CreateGameComponent} from "../components/create-game/create-game.component";
import {MatDialog} from "@angular/material/dialog";
import {Router} from "@angular/router";
import {UserPlayer} from "../../models/input/user-player.model";

@Component({
  selector: 'app-home',
  templateUrl: './home.page.html',
  styleUrls: ['./home.page.css']
})
export class HomePage implements OnInit {

  private activeGames: ActiveGame[] = [];

  public player: UserPlayer | null = null;

  public isMobile: boolean;

  public isAdmin: boolean = false;

  constructor(private readonly homeAPI: HomeAPI, private dialog: MatDialog, private readonly router: Router) {
    this.isMobile = window.innerWidth < 768;
  }

  ngOnInit(): void {
    //Filling the list with active games
    this.homeAPI.getGames()
      .subscribe((games) => {
        for (let game of games) {
          this.activeGames.push({
              id: game.id,
              name: game.name,
              gameState: game.gameState,
              playerAmount: game.playerAmount,
              startTime: game.startDate,
              endTime: game.endDate,
              playerID: null
            });
        }
      });
  }

  userLoaded(data :{admin: boolean, players: UserPlayer[]}) {
    this.isAdmin = data.admin;
    for (let player of data.players) {
      const game: ActiveGame | undefined = this.activeGames.find(g => g.id === player.gameID)
      if (game != undefined) {
        game.playerID = player.id
      }
    }
  }

  createGame(): void {
    let gameCreated = false;
    const dialogRef = this.dialog.open(CreateGameComponent);
    dialogRef.afterClosed().subscribe(result => {
      if (result != undefined && !gameCreated) {
        gameCreated = true; //Ensures only one request is sent to the server.
        this.homeAPI.createGame(result)
          .then(res => res.subscribe(
            data => this.router.navigate(["game/"+data.id+"/admin"])
          ));
      }
    });
  }

  openUserManual(): void {
    window.open("../../../assets/HvZ-usermanual.pdf", '_blank');
  }


public get games(): ActiveGame[] {
    return this.activeGames;
  }

  public localError() {
    throw Error('The home page has thrown an error!');
  }
}
