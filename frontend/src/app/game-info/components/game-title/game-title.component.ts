import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-game-title',
  templateUrl: './game-title.component.html',
  styleUrls: ['./game-title.component.css']
})
export class GameTitleComponent {

  @Input()
  public gameTitle: string = "ERROR: No game title found";
  @Output() //Emits when the "join game" button is pressed
  public joinGame = new EventEmitter<any>();
  @Input()
  public playerHasJoined!: boolean;

  constructor(private readonly router: Router) { }

  joinGameClicked(): void {
    this.playerHasJoined = true;
    this.joinGame.emit();
  }

  toHome() {
    return this.router.navigate([""]);
  }

}
