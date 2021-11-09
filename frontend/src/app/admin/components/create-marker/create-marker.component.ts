import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import LatLng = google.maps.LatLng;

@Component({
  selector: 'app-create-marker',
  templateUrl: './create-marker.component.html',
  styleUrls: ['./create-marker.component.css']
})
export class CreateMarkerComponent {

  //Popup-window class that takes in the position of the marker as a parameter
  constructor(@Inject(MAT_DIALOG_DATA) public data: {position: LatLng}) {
  }
}
