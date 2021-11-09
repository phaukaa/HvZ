import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-game-description',
  templateUrl: './game-description.component.html',
  styleUrls: ['./game-description.component.css']
})

//Simply shows the description and rules of a game.
export class GameDescriptionComponent {

  @Input()
  public gameDescription: string = "ERROR: No game description";

  constructor() { }

}
