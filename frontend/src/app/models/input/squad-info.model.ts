import {PlayerInfo} from "./player-info.model";

export interface SquadInfo {
  name: string,
  members: PlayerInfo[],
  id: number,
  numDead: number
}
