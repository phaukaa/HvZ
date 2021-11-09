import {Component, EventEmitter, Input, OnChanges, OnInit, Output} from '@angular/core';
import {SquadInfo} from "../../../models/input/squad-info.model";
import {GameInfoAPI} from "../../api/game-info.api";
import LatLng = google.maps.LatLng;
import {SquadCheckInOutput} from "../../../models/output/squad-check-in-output";

@Component({
  selector: 'app-squad',
  templateUrl: './squad.component.html',
  styleUrls: ['./squad.component.css']
})
export class SquadComponent implements OnInit, OnChanges {

  @Input()
  public squad: SquadInfo | null = null;
  @Input()
  public allSquads = null;
  @Input()
  gameID!: number;
  @Input()
  playerID!: number | null;
  @Input()
  playerLocation: LatLng | null = null;
  @Input()
  playerHasGame: boolean = false;
  @Output()
  requestLocation = new EventEmitter<any>();
  @Output()
  updateCheckIns = new EventEmitter<any>();

  checkInRequested = false;
  public join = false;
  public create = false;
  public createSquadName = "";
  public isMobile: boolean;

  constructor(private readonly gameInfoAPI: GameInfoAPI) {
    this.isMobile = window.innerWidth < 768;
  }

  ngOnInit(): void {
  }

  ngOnChanges() {
    if (this.playerLocation != null && this.squad != null && this.checkInRequested && this.playerID != null) {
      const checkIn: SquadCheckInOutput = {
        playerID: this.playerID,
        lat: this.playerLocation.lat(),
        lng: this.playerLocation.lng(),
      }
      this.gameInfoAPI.squadCheckIn(this.gameID, this.squad.id, checkIn)
        .then(res => res.subscribe( () => {
          this.updateCheckIns.emit();
        }));
      this.checkInRequested = false;
    }
  }

  squadCheckIn() {
    this.requestLocation.emit();
    this.checkInRequested = true;
  }

  @Output() updateSquads: EventEmitter<any> = new EventEmitter<any>();
  joinClick() {
    this.join = true;
    this.updateSquads.emit();
  }

  @Output() joinThis: EventEmitter<any> = new EventEmitter<any>();
  joinSquad(id: number) {
    this.joinThis.emit(id);
    this.join = false;
    this.create = false;
  }

  @Output() createThis: EventEmitter<any> = new EventEmitter<any>();
  createSquad() {
    if (this.createSquadName != "") {
      this.createThis.emit(this.createSquadName);
      this.createSquadName = "";
      this.join = false;
      this.create = false;
    }
  }

  @Output() leaveThis: EventEmitter<any> = new EventEmitter<any>();
  leaveSquad() {
    if (this.squad != null) {
      this.leaveThis.emit();
    }
  }
}

