import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {GameInfoPage} from "./pages/game-info.page";

// Inherit from AppRoutingModule -> /home
const routes: Routes = [
  {
    path: '',
    component: GameInfoPage
  }
];

@NgModule({
  imports: [
    RouterModule.forChild( routes )
  ],
  exports: [
    RouterModule
  ]
})
export class GameInfoRoutingModule {
}
