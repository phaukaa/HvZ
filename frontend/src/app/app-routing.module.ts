import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {AuthGuard} from "./shared/auth-guard/auth-guard.service";

const routes: Routes = [
  { path: '',
    loadChildren: () => import("./home/home.module").then(m => m.HomeModule)
  },
  { path: 'game/:id/player/:playerId',
    canActivate:[AuthGuard], data:{requiresLogin: true},
    loadChildren: () => import("./game-info/game-info.module").then(m => m.GameInfoModule)
  },
  { path: 'game/:id',
    canActivate:[AuthGuard], data:{requiresLogin: true},
    loadChildren: () => import("./game-info/game-info.module").then(m => m.GameInfoModule)
  },
  { path: 'game/:id/admin',
    canActivate:[AuthGuard], data:{requiresLogin: true},
    loadChildren: () => import("./admin/admin.module").then(m => m.AdminModule)
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
