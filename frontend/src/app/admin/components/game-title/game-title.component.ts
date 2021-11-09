import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-game-title-admin',
  templateUrl: './game-title.component.html',
  styleUrls: ['./game-title.component.css']
})
export class GameTitleComponent {

  //A custom [(ngModel)] that keeps track of what an updated title contains.
  @Input()
  public gameTitle: string = "ERROR: No game title found";
  @Output()
  public gameTitleChange = new EventEmitter<string>();
  @Input()
  public gameState!: string;
  @Output()
  public gameStateChange = new EventEmitter<string>();

  constructor(private readonly router: Router) { }

  //Called when the save button is clicked
  saveChanges(): void {
    this.gameStateChange.emit(this.gameState);
    this.gameTitleChange.emit(this.gameTitle);
  }
  toHome() {
    return this.router.navigate([""]);
  }
}
