import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.css']
})
export class GameComponent {

  //A custom [(ngModel)] that keeps track of what an updated description contains.
  @Input()
  gameDescription!: string;
  @Output()
  gameDescriptionChange = new EventEmitter<string>();

  constructor() { }

  //Called when the contents of the description textarea changes.
  outputDesc(): void {
    this.gameDescriptionChange.emit(this.gameDescription)
  }

}
