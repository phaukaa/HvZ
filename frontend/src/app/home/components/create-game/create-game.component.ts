import { Component } from '@angular/core';
import {DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE} from "@angular/material/core";
import {MAT_MOMENT_DATE_ADAPTER_OPTIONS, MomentDateAdapter} from "@angular/material-moment-adapter";
import {MatDialogRef} from "@angular/material/dialog";

export const MY_FORMATS = {
  parse: {
    dateInput: 'DD/MM/YYYY',
  },
  display: {
    dateInput: 'DD/MM/YYYY',
    monthYearLabel: 'DD MM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'DDDD MMMM YYYY',
  },
};

@Component({
  selector: 'app-create-game',
  templateUrl: './create-game.component.html',
  styleUrls: ['./create-game.component.css'],
  providers: [{
    provide: DateAdapter,
    useClass: MomentDateAdapter,
    deps: [MAT_DATE_LOCALE, MAT_MOMENT_DATE_ADAPTER_OPTIONS]
  },
    {provide: MAT_DATE_FORMATS, useValue: MY_FORMATS},
  ],
})
export class CreateGameComponent {

  buttonClicked = false;
  illegalDate = false;
  data: {name: string, description: string | null, startTime: null | string, endTime: null | string} =
    {name: "", description: null, startTime: null, endTime: null};

  constructor(public dialogRef: MatDialogRef<CreateGameComponent>) { }

  closeDialog() {
    this.buttonClicked = true;
    if (this.checkDate()) {
      if (this.data.name.length > 0) {
        this.dialogRef.close(this.data);
      }
    } else {
      this.illegalDate = true;
    }
  }

  checkDate(): boolean {
    if (this.data.startTime != null) this.data.startTime = JSON.stringify(this.data.startTime).split("\"")[1];
    if (this.data.endTime != null) this.data.endTime = JSON.stringify(this.data.endTime).split("\"")[1];
    if (this.data.startTime != null && this.data.endTime != null) {
      if (this.data.startTime > this.data.endTime) {
        return false;
      }
    }
    return true;
  }

}
