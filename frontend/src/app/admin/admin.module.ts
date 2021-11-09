import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {AdminPage} from './pages/admin.page';
import {AdminRoutingModule} from "./admin-routing.module";
import { MapComponent } from './components/map/map.component';
import { ChatComponent } from './components/chat/chat.component';
import { GameComponent } from './components/game/game.component';
import { PlayersComponent } from './components/players/players.component';
import {GameTitleComponent} from "./components/game-title/game-title.component";
import {GoogleMapsModule} from "@angular/google-maps";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { MissionEditComponent } from './components/mission-edit/mission-edit.component';
import {MatDialogModule} from "@angular/material/dialog";
import {MatInputModule} from "@angular/material/input";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatSlideToggleModule} from "@angular/material/slide-toggle";
import { CreateMarkerComponent } from './components/create-marker/create-marker.component';
import { KillEditComponent } from './components/kill-edit/kill-edit.component';
import {MatCardModule} from "@angular/material/card";
import { SinglePlayerComponent } from './components/single-player/single-player.component';
import {MatSelectModule} from "@angular/material/select";
import { PlayerEditComponent } from './components/player-edit/player-edit.component';
import {PlayerCreateComponent} from "./components/player-create/player-create.component";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {GameInfoModule} from "../game-info/game-info.module";
import {SquadComponent} from "./components/squad/squad.component";
import {ViewsquadComponent} from "./components/squad-view/viewsquad.component";

@NgModule({
  declarations: [
    AdminPage,
    MapComponent,
    ChatComponent,
    GameComponent,
    PlayersComponent,
    GameTitleComponent,
    MissionEditComponent,
    CreateMarkerComponent,
    KillEditComponent,
    SinglePlayerComponent,
    PlayerEditComponent,
    PlayerCreateComponent,
    SquadComponent,
    ViewsquadComponent
  ],
    imports: [
        CommonModule,
        AdminRoutingModule,
        GoogleMapsModule,
        FormsModule,
        MatDialogModule,
        MatInputModule,
        MatDatepickerModule,
        ReactiveFormsModule,
        MatSlideToggleModule,
        MatCardModule,
        MatSelectModule,
        MatCheckboxModule,
        GameInfoModule
    ],
  entryComponents: [
    MissionEditComponent
  ]
})
export class AdminModule { }
