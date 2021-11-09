import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {PlayerAdminOutput} from "../../../models/output/player-admin-output.model";
import {User} from "../../../models/input/user.model";

@Component({
  selector: 'app-player-edit',
  templateUrl: './player-create.component.html',
  styleUrls: ['./player-create.component.css']
})
export class PlayerCreateComponent {

  constructor(public dialogRef: MatDialogRef<PlayerCreateComponent>, @Inject(MAT_DIALOG_DATA) public data: {gameID: number, allUsers: User[]}) {
    this.gameID = data.gameID;
    this.player.userID = this.data.allUsers[0].openID
  }

  public gameID!: number;

  player: PlayerAdminOutput = {
    human: true,
    userID: "",
    patientZero: false,
    id: 0
  }

  patientZeroChanged() {
    if (this.player.patientZero) this.player.human = false;
  }

  selectPlayer(event: Event) {
    this.player.userID = (event.target as HTMLSelectElement).value;
  }

  /*
  Returns the values of the created player
   */
  closeDialog() {
    if (this.player.userID != undefined && this.player.userID.length > 0) {
        this.dialogRef.close(this.player);
    }
  }
}
