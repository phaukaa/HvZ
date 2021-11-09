import {Component, Input} from '@angular/core';
import {PlayerInfoFull} from "../../../models/input/player-info-full.model";

@Component({
  selector: 'app-single-player',
  templateUrl: './single-player.component.html',
  styleUrls: ['./single-player.component.css']
})

//A component that holds information about a single player.
export class SinglePlayerComponent {

  public playerName: string = "";

  @Input()
  public playerInfo!: PlayerInfoFull;

  constructor() { }

  get player(): PlayerInfoFull {
    return this.playerInfo;
  }
}
