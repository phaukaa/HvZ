import {Component, EventEmitter, Input, Output} from '@angular/core';
import {PlayerInfoFull} from "../../../models/input/player-info-full.model";
import {MatDialog} from "@angular/material/dialog";
import {AdminAPI} from "../../api/admin.api";
import {PlayerEditComponent} from "../player-edit/player-edit.component";
import {PlayerCreateComponent} from "../player-create/player-create.component";

@Component({
  selector: 'app-players',
  templateUrl: './players.component.html',
  styleUrls: ['./players.component.css']
})
export class PlayersComponent {

  @Input() //All the players in the current game
  public playerList!: PlayerInfoFull[];

  @Input()
  public gameID!: number;

  //If the Admin Page needs to update the player list with an api call.
  @Output()
  public playerUpdate: EventEmitter<any> = new EventEmitter<any>();

  public isMobile: boolean;

  constructor(private dialog: MatDialog, private readonly adminAPI: AdminAPI) {
    this.isMobile = window.innerWidth < 768;
  }

  /**
   * Creates a popup window for editing a player.
   * @param player info about the player that should be edited
   */
  editPlayer(player: PlayerInfoFull): void {
    const dialogRef = this.dialog.open(PlayerEditComponent, {data: player});
    dialogRef.afterClosed().subscribe(result => {
      if (result != undefined) {
        if (result) {
          this.adminAPI.updatePlayer(this.gameID, player.id, result)
            .then(res => res.subscribe(
              () => this.playerUpdate.emit()
            ));
        }
        else {
          this.adminAPI.deletePlayer(this.gameID, player.id)
            .then(res => res.subscribe(
              () => this.playerUpdate.emit()
            ));
        }
      }
    });
  }

  createPlayer(): void {
    this.adminAPI.getAllUsers()
      .then(res => res.subscribe(
        resp => {
          const dialogRef = this.dialog.open(PlayerCreateComponent, {data: {gameID: this.gameID, allUsers: resp}});
          dialogRef.afterClosed().subscribe(result => {
            if (result != undefined) {
              this.adminAPI.createPlayer(this.gameID, result)
                .then(res => res.subscribe(
                  () => this.playerUpdate.emit()
                ));
            }
          });
        }
      ))
  }

  get players(): PlayerInfoFull[] {
    return this.playerList;
  }
}
