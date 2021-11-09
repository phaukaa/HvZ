import {MapMarker} from "../../models/input/map-marker.model";
import {Kill} from "../../models/input/kill.model";
import {Mission} from "../../models/input/mission.model";
import {SquadCheckIn} from "../../models/input/squad-check-in.model";
import {MapBorder} from "../../models/input/map-border.model";

//Takes an array of kills and missions and turns them into map markers.
//Used in both the admin and game-info modules.
export function createMapMarkers(kills: Kill[], missions: Mission[], squadCheckIns: SquadCheckIn[], mapBorders: MapBorder): MapMarker[] {
  const markers: MapMarker[] = [];
  // Populating the marker list
  for (let mission of missions) {
    markers.push({
      id: mission.id,
      type: "MISSION",
      description: mission.name + "<br><br>" + mission.description,
      position: {lat: mission.lat, lng: mission.lng},
      options: {icon: "../assets/mission-icon.png", opacity: 1.0},
      title: ""
    });
  }
  for (let kill of kills) {
    if (kill.lat != null && kill.lng != null) { //Position data for kills is optional.
      markers.push({
        id: kill.id,
        type: "KILL",
        description: kill.killerName + " killed " + kill.victimName + "<br><br>" + kill.story,
        position: {lat: kill.lat, lng: kill.lng},
        options: {icon: "../assets/tombstone-icon.png", opacity: 1.0},
        title: ""
      });
    }
  }
  for (let checkIn of squadCheckIns) {
    markers.push({
      id: checkIn.id,
      type: "CHECKIN",
      description: checkIn.member.name + " @ " + checkIn.time.split("T")[1].split(".")[0],
      position: {lat: checkIn.lat, lng: checkIn.lng},
      options: {icon: "../assets/check-in-icon.png", opacity: 1.0},
      title: ""
    })
  }
  if (mapBorders.nw_lat != null && mapBorders.nw_long != null && mapBorders.se_lat != null && mapBorders.se_long != null) {
    markers.push({
      description: "",
      id: 0,
      options: {icon: "", opacity: 0.0},
      position: {lat: mapBorders.nw_lat, lng: mapBorders.nw_long},
      title: "",
      type: "BORDER"
    })
    markers.push({
      description: "",
      id: 0,
      options: {icon: "", opacity: 0.0},
      position: {lat: mapBorders.nw_lat, lng: mapBorders.se_long},
      title: "",
      type: "BORDER"
    })
    markers.push({
      description: "",
      id: 0,
      options: {icon: "", opacity: 0.0},
      position: {lat: mapBorders.se_lat, lng: mapBorders.nw_long},
      title: "",
      type: "BORDER"
    })
    markers.push({
      description: "",
      id: 0,
      options: {icon: "", opacity: 0.0},
      position: {lat: mapBorders.se_lat, lng: mapBorders.se_long},
      title: "",
      type: "BORDER"
    })
  }
  return markers;
}
