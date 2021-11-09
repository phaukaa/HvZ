import {SquadInfo} from "./squad-info.model";
import {MapBorder} from "./map-border.model";
import {Kill} from "./kill.model";
import {Mission} from "./mission.model";
import {PlayerInfoFull} from "./player-info-full.model";
import {SquadCheckIn} from "./squad-check-in.model";

export interface GameInfoAdmin {
  id: number,
  name: string,
  state: string,
  description: string,
  squad_info: SquadInfo | null,
  map_info: MapBorder,
  kills: Kill[],
  missions: Mission[],
  players: PlayerInfoFull[],
  squadCheckIns: SquadCheckIn[]
}
