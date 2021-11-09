import {Component, EventEmitter, Input, Output} from '@angular/core';
import LatLng = google.maps.LatLng;
import {KillOutput} from "../../../models/output/kill-output.model";
import {GameInfoAPI} from "../../api/game-info.api";

@Component({
  selector: 'app-bite-code',
  templateUrl: './bite-code.component.html',
  styleUrls: ['./bite-code.component.css']
})
export class BiteCodeComponent {

  @Input()
  public isHuman!: boolean;
  @Input()
  playerID!: number | null;
  @Input()
  public biteCode: string = "";
  @Input()
  locationOfKill: LatLng | null = null;
  @Input()
  public biteCodeError: boolean = false;
  @Input()
  public gameID!: number
  @Output() //Emits when a kill has been added
  updateKills = new EventEmitter<any>();

  @Output() //Emits when the location button is clicked
  requestLocation = new EventEmitter<any>();

  public biteCodeInput: string = "";
  public storyInput: string = "";
  public isMobile: boolean;

  constructor(private readonly gameInfoAPI: GameInfoAPI) {
    this.isMobile = window.innerWidth < 768;
  }

  //Saves the new kill
  saveKill(): void {
    if (this.biteCodeInput.length === 10) {
      let lat = null;
      let lng = null;
      if (this.locationOfKill != null) {
        lat = this.locationOfKill.lat();
        lng = this.locationOfKill.lng();
      }
      const kill: KillOutput = {
        biteCode: this.biteCodeInput,
        killerID: this.playerID!,
        id: 0,
        lat: lat,
        lng: lng,
        story: this.storyInput,
        timeOfDeath: new Date().getTime().toString()
      };
      this.gameInfoAPI.createKill(this.gameID, kill)
        .then(res => res.subscribe(
          () => {
            this.biteCodeError = false;
            this.updateKills.emit()
          },
          () => {
            this.biteCodeError = true;
          }
        ))
    }
    else {
      this.biteCodeError = true;
    }
  }

  //Requests the location of the player.
  getLocation(): void {
    this.requestLocation.emit();
  }

}
