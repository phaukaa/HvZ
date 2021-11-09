import { NgModule } from '@angular/core';
import { ErrorDialogComponent } from './errors/error-dialog/error-dialog.component';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ErrorDialogService } from './errors/error-dialog.service';
import {MatDialogModule} from "@angular/material/dialog";
import {MatButtonModule} from "@angular/material/button";

const sharedComponents = [ ErrorDialogComponent];

@NgModule({
  declarations: [...sharedComponents],
  imports: [CommonModule, RouterModule, MatDialogModule, MatButtonModule],
  exports: [...sharedComponents],
  providers: [ErrorDialogService],
  entryComponents: [...sharedComponents],
})
export class ErrorModule {}
