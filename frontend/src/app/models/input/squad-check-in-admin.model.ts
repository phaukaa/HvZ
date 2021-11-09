import {SquadCheckIn} from "./squad-check-in.model";

export interface SquadCheckInAdmin extends SquadCheckIn {
  id: number,
  time: string,
  lat: number,
  lng: number,
  member: {
    id: number,
    name: string,
    gameID: number,
    human: boolean
  }
}
