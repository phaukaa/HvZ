import {Component, Input} from "@angular/core";
import {MatDialog} from "@angular/material/dialog";
import {ViewsquadComponent} from "../squad-view/viewsquad.component";

@Component({
  selector: 'app-squad-admin',
  templateUrl: './squad.component.html',
  styleUrls: ['./squad.component.css']
})
export class SquadComponent {

  constructor(public dialog: MatDialog) {
  }

  @Input()
  public squads: any[] | null = null;

  viewSquad(squadID: number) {
    let squad = this.squads!.find(i => i.id == squadID)
    const dialog = this.dialog.open(ViewsquadComponent, {
      height: "fit-content",
      width: "fit-content",
      data: {
        name: squad.name,
        members: squad.members
      }
    })
  }
}
