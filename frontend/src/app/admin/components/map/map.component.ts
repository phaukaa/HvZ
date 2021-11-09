import {Component, EventEmitter, Input, OnChanges, OnInit, Output} from '@angular/core';
import {Observable, of} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {catchError, map} from "rxjs/operators";
import {mapRectangleOptions, options} from "src/assets/map-options";
import {MapBorder} from "../../../models/input/map-border.model";
import {Kill} from "../../../models/input/kill.model";
import {Mission} from "../../../models/input/mission.model";
import {MapMarker} from "../../../models/input/map-marker.model";
import {MissionEditComponent} from "../mission-edit/mission-edit.component";
import {MatDialog} from "@angular/material/dialog";
import {AdminAPI} from "../../api/admin.api";
import LatLng = google.maps.LatLng;
import {CreateMarkerComponent} from "../create-marker/create-marker.component";
import {KillEditComponent} from "../kill-edit/kill-edit.component";
import {KillOutput} from "../../../models/output/kill-output.model";
import {createMapMarkers} from "../../../shared/maps/map.functions";
import {SquadCheckIn} from "../../../models/input/squad-check-in.model";
import LatLngBounds = google.maps.LatLngBounds;

@Component({
  selector: 'app-map-admin',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements OnInit, OnChanges {

  //Current borders of the map as a custom [(ngModel)]
  @Input()
  mapInfo!: MapBorder;
  @Output()
  mapInfoChange = new EventEmitter<MapBorder>();

  @Input() //Kills in the game
  kills!: Kill[];
  @Input() //Missions in the game
  missions!: Mission[];
  @Input()
  squadCheckIns!: SquadCheckIn[];
  @Input()
  public gameID!: number;
  @Input() //The names of the humans in the game and their corresponding bite codes
  public biteCodes!: {name: string, biteCode: string}[];
  @Input() //The names of the zombies in the game and their corresponding ids
  public ids!: {name: string, id: number}[];
  @Output() //Emits whenever a mission is updated
  missionUpdate: EventEmitter<any> = new EventEmitter<any>();
  @Output() //Emits whenever a kill is updated
  killUpdate: EventEmitter<any> = new EventEmitter<any>();

  apiLoaded!: Observable<boolean>;

  //Initial settings for the Google Map
  options: google.maps.MapOptions = options;

  mapRectangleOptions: google.maps.RectangleOptions = mapRectangleOptions;

  //Markers for the Google Map are put here
  markers: MapMarker[] = [];

  //If the toggle for changing the map borders is on or not
  changeArea: boolean = false;
  //What corner should be edited next
  lastNorthWest = false;
  //The current borders of the map
  corners: {nw: LatLng | null, se: LatLng | null} = {nw: null, se: null}

  public isMobile: boolean;
  public centerNotChanged = true;
  public bounds = new LatLngBounds(new LatLng(0,0), new LatLng(0,0));
  public showBounds = false;
  public center!: LatLng;

  constructor(private readonly httpClient: HttpClient, public dialog: MatDialog, private readonly adminAPI: AdminAPI) {
    this.isMobile = window.innerWidth < 768;
  }

  /**
   * Creates the map and applies the borders (If any)
   * This is a duplicate code, but it creates a specific part for this component, so it should be here.
   */
  ngOnInit() {
    if (this.mapInfo.nw_lat != null && this.mapInfo.nw_long != null && this.mapInfo.se_lat != null && this.mapInfo.se_long != null) {
      this.options.restriction = {latLngBounds: {
          east: this.mapInfo.se_long,
          north: this.mapInfo.nw_lat,
          south: this.mapInfo.se_lat,
          west: this.mapInfo.nw_long
        }};
      this.center = new LatLng(this.mapInfo.nw_lat - (this.mapInfo.nw_lat - this.mapInfo.se_lat),this.mapInfo.nw_long - (this.mapInfo.nw_long - this.mapInfo.se_long));
      this.showBounds = true;
    }
    else {
      this.center = new LatLng(52.9, 51.8);
    }
    this.apiLoaded = this.httpClient.jsonp('https://maps.googleapis.com/maps/api/js?key=AIzaSyDLrbUDvEj78cTcTCheVdJbIH5IT5xPAkQ', 'initMap')
      .pipe(
        map(() => true),
        catchError(() => of(false)),
      );
  }

  /**
   * Updates the markers-array for the map
   */
  ngOnChanges() {
    //Resetting the markers so that they dont get loaded twice when changes are made.
    this.markers = createMapMarkers(this.kills, this.missions, this.squadCheckIns, this.mapInfo);
    if (this.mapInfo.nw_lat != null && this.mapInfo.nw_long != null && this.mapInfo.se_lat != null && this.mapInfo.se_long != null) {
      const ne = new LatLng(this.mapInfo.nw_lat, this.mapInfo.se_long)
      const sw = new LatLng(this.mapInfo.se_lat, this.mapInfo.nw_long)
      this.corners = {
        nw: ne,
        se: sw
      };
      this.showBounds = true;
      this.bounds = new LatLngBounds(ne,sw);
      if (this.centerNotChanged) {
        this.center = this.bounds.getCenter();
        this.centerNotChanged = false;
      }
    }
  }

  /**
   * Just switches the value of the variable
   */
  toggleAreaChange(): void {
    this.changeArea = !this.changeArea;
    this.lastNorthWest = false;
  }

  /**
   * Decides whether a marker should be placed or the borders should be edited.
   * @param position where the map was clicked
   * @param fromRectangle if the area clicked was inside the map borders.
   */
  mapClick(position: LatLng, fromRectangle: boolean): void {
    if (this.changeArea) {
      this.placeCorner(position);
    }
    else if (this.showBounds && fromRectangle || !this.showBounds) {
      this.createMarker(position)
    }
  }

  /**
   * Places a corner for the map borders
   * @param position where the corner is
   */
  placeCorner(position: LatLng) {
    if (this.lastNorthWest) {
      this.corners.nw = position;
      this.mapInfo!.nw_lat = position.lat()
      this.mapInfo!.nw_long = position.lng()
      this.showBounds = true;
      this.changeArea = false;
      this.mapInfoChange.emit(this.mapInfo);
    }
    else {
      this.corners.se = position;
      this.mapInfo!.se_lat = position.lat()
      this.mapInfo!.se_long = position.lng()
    }
    this.lastNorthWest = !this.lastNorthWest;
  }

  /**
   * Checks if the selected marker is for a kill or a mission and opens the proper method.
   * @param id what is the id of the marker (Used to find it in the kills and missions lists)
   * @param type should a mission or kill be edited
   */
  public editMarker(id: number, type: string): void {
    if (type === "MISSION") {
      const mission = this.missions.find(m => m.id === id);
      if (mission != undefined) {
        this.editMission(mission);
      }
    }
    else if (type === "KILL") {
      const kill = this.kills.find(k => k.id === id);
      if (kill != undefined) {
        this.editKill(kill);
      }
    }
  }

  /**
   * Checks if the admin wants to create a kill- or mission marker.
   * @param position Where the marker is placed
   */
  public createMarker(position: LatLng): void {
    const dialogRef = this.dialog.open(CreateMarkerComponent, {
      height: "fit-content",
      width: "fit-content",
      data: {
        position: position
      }
    });
    dialogRef.afterClosed().subscribe(result => {
        if (result) {
          this.createMission(position);
        }
        else if ( result === false){
          this.createKill(position);
        }
    });
  }

  /**
   * Opens a dialog window for the specified kill
   * @param kill the kill that should be edited
   */
  editKill(kill: Kill): void {
    let outputKill: KillOutput = {
      biteCode: this.biteCodes.find(b => b.name === kill.victimName)!.biteCode,
      id: kill.id,
      killerID: this.ids.find(b => b.name === kill.killerName)!.id,
      lat: kill.lat,
      lng: kill.lng,
      story: kill.story,
      timeOfDeath: ""
    }
    const dialogRef = this.dialog.open(KillEditComponent, {
      height: "fit-content",
      width: "fit-content",
      data: {
        killerName: kill.killerName,
        victimName: kill.victimName,
        timeOfDeath: kill.timeOfDeath,
        story: kill.story,
        ids: this.ids,
        biteCodes: this.biteCodes
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result != undefined) {
        if (!result) {
          this.adminAPI.deleteKill(this.gameID, kill.id)
            .then(result => result.subscribe(() => {
              this.killUpdate.emit();
            }));
        }
        else {
          outputKill.killerID = parseInt(result.killerName); //For ease of coding, killerName holds the id and victimName holds the biteCode
          outputKill.biteCode = result.victimName;
          outputKill.story = result.story;
          outputKill.timeOfDeath = result.timeOfDeath;
          this.adminAPI.updateKill(this.gameID, outputKill.id, outputKill)
            .then(result => result.subscribe(() => {
              this.killUpdate.emit();
            }));
        }
      }
    });
  }

  /**
   * Creates a new kill
   * @param position where the marker is
   */
  createKill(position: LatLng): void {
    let kill: KillOutput = {
      timeOfDeath: "",
      killerID: 0,
      biteCode: "",
      story: "",
      id: 0,
      lat: position.lat(),
      lng: position.lng()
    }
    const dialogRef = this.dialog.open(KillEditComponent, {
      height: "fit-content",
      width: "fit-content",
      data: {
        timeOfDeath: null,
        killerName: null,
        victimName: null,
        story: null,
        biteCodes: this.biteCodes,
        ids: this.ids
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result != undefined) {
        kill.killerID = parseInt(result.killerName); //For ease of coding, killerName holds the id and victimName holds the biteCode
        kill.biteCode = result.victimName;
        kill.story = result.story;
        kill.timeOfDeath = result.timeOfDeath;
        this.adminAPI.createKill(this.gameID, kill)
          .then(result => result.subscribe(() => {
            this.killUpdate.emit();
          }));
      }
    });
  }

  /**
   * Opens a dialog window for the specified mission.
   * @param mission the mission that should be edited
   */
  editMission(mission: Mission): void {
    const dialogRef = this.dialog.open(MissionEditComponent, {
      height: "fit-content",
      width: "fit-content",
      data: {
        name: mission.name,
        description: mission.description,
        startTime: mission.startTime,
        endTime: mission.endTime,
        isHuman: mission.human
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result != undefined) {
        if (!result) {
          this.adminAPI.deleteMission(this.gameID, mission.id)
            .then(result => result.subscribe(() => {
              this.missionUpdate.emit();
            }));
        }
        else {
          mission.name = result.name;
          mission.human = result.isHuman;
          mission.description = result.description;
          mission.startTime = result.startTime;
          mission.endTime = result.endTime;
          this.adminAPI.updateMission(this.gameID, mission.id, mission)
            .then(result => result.subscribe(() => {
              this.missionUpdate.emit();
            }));
        }
      }
    });
  }

  /**
   * Creates a new mission
   * @param position where the mission is
   */
  createMission(position: LatLng): void {
    let mission: Mission = {
      name: "",
      description: "",
      startTime: null,
      endTime: null,
      gameId: this.gameID,
      human: false,
      id: 0,
      lat: position.lat(),
      lng: position.lng()
    }
    const dialogRef = this.dialog.open(MissionEditComponent, {
      height: "fit-content",
      width: "fit-content",
      data: {
        name: "",
        description: "",
        startTime: "",
        endTime: "",
        isHuman: false
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result != undefined) {
        mission.name = result.name;
        mission.human = result.isHuman;
        mission.description = result.description;
        mission.startTime = result.startTime;
        mission.endTime = result.endTime;
        this.adminAPI.createMission(this.gameID, mission)
          .then(result => result.subscribe(() => {
            this.missionUpdate.emit();
          }));
      }
    });
  }

  //Used as a callback function for the map in ngOnInit.
  initMap(): void {
    // Styles a map in night mode.
    new google.maps.Map(
      document.getElementById("map") as HTMLElement,
      this.options
    );
  }
}
