import {SquadInfo} from "./squad-info.model";
import {MapBorder} from "./map-border.model";
import {Message} from "./message.model";
import {Kill} from "./kill.model";
import {Mission} from "./mission.model";
import {SquadCheckIn} from "./squad-check-in.model";

export interface GameInfo {
  gameID: number,
  playerID: number | null,
  player_is_human: boolean,
  name: string,
  state: string,
  description: string,
  /*player_count: number,*/ //Not implemented
  bite_code: string,
  squad_info: SquadInfo | null,
  map_info: MapBorder,
  messages: Message[],
  kills: Kill[],
  missions: Mission[],
  squadCheckIns: SquadCheckIn[]
}
