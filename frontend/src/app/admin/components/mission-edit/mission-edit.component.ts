import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE} from "@angular/material/core";
import {MAT_MOMENT_DATE_ADAPTER_OPTIONS, MomentDateAdapter} from "@angular/material-moment-adapter";
import {DATE_FORMAT} from "../../../../assets/date.format";

@Component({
  selector: 'app-mission-edit',
  templateUrl: './mission-edit.component.html',
  styleUrls: ['./mission-edit.component.css'],
  providers: [{
      provide: DateAdapter,
      useClass: MomentDateAdapter,
      deps: [MAT_DATE_LOCALE, MAT_MOMENT_DATE_ADAPTER_OPTIONS]
    },
    {provide: MAT_DATE_FORMATS, useValue: DATE_FORMAT},
  ],
})
export class MissionEditComponent {

  //Whether error messages should be shown
  buttonClicked = false;
  illegalDate = false;

  constructor(public dialogRef: MatDialogRef<MissionEditComponent>, @Inject(MAT_DIALOG_DATA) public data: {name: string | null, description: string | null, startTime: string | null, endTime: string | null, isHuman: boolean}) {
  }

   /*Checks if the provided dates are correct. Needs no parameters because of [(ngModel)].
   Also formats the dates properly
    */
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

  /**
   * Is run whenever the save or delete buttons are clicked. Checks whether the created mission has all necessary parameters
   * @param edit whether the mission should be edited/created or deleted. If false, it means the delete button has been pressed
   */
  closeDialog(edit: boolean) {
    if (edit) {
      this.buttonClicked = true;
      if (this.checkDate()) {
        if (this.data.name != undefined && this.data.name.length > 0) {
          if (this.data.description != undefined && this.data.description.length > 0)
            this.dialogRef.close(this.data);
        }
      }
      else {
        this.illegalDate = true;
      }
    }
    else {
      this.dialogRef.close(false);
    }
  }
}
