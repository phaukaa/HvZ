import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GameInfoPage } from './pages/game-info.page';
import { GameDescriptionComponent } from './components/game-description/game-description.component';
import { GameTitleComponent } from './components/game-title/game-title.component';
import { SquadComponent } from './components/squad/squad.component';
import { ChatComponent } from './components/chat/chat.component';
import { BiteCodeComponent } from './components/bite-code/bite-code.component';
import {GameInfoRoutingModule} from "./game-info-routing.module";
import { MapComponent } from './components/map/map.component';
import {GoogleMapsModule} from "@angular/google-maps";
import { MessageComponent } from '../shared/message/message.component';
import {FormsModule} from "@angular/forms";
import {MatFormFieldModule} from "@angular/material/form-field";
import {TextFieldModule} from "@angular/cdk/text-field";

@NgModule({
  declarations: [
    GameInfoPage,
    GameDescriptionComponent,
    GameTitleComponent,
    SquadComponent,
    ChatComponent,
    BiteCodeComponent,
    MapComponent,
    MessageComponent
  ],
    imports: [
        CommonModule,
        GameInfoRoutingModule,
        GoogleMapsModule,
        FormsModule,
        MatFormFieldModule,
        TextFieldModule
    ],
  exports: [
    BiteCodeComponent,
    ChatComponent,
    GameDescriptionComponent,
    GameTitleComponent,
    SquadComponent,
    MessageComponent
  ]
})
export class GameInfoModule { }
