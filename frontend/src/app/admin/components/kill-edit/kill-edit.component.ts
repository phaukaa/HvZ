import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE} from "@angular/material/core";
import {MAT_MOMENT_DATE_ADAPTER_OPTIONS, MomentDateAdapter} from "@angular/material-moment-adapter";
import {DATE_FORMAT} from "../../../../assets/date.format";

@Component({
  selector: 'app-kill-edit',
  templateUrl: './kill-edit.component.html',
  styleUrls: ['./kill-edit.component.css'],
  providers: [{
    provide: DateAdapter,
    useClass: MomentDateAdapter,
    deps: [MAT_DATE_LOCALE, MAT_MOMENT_DATE_ADAPTER_OPTIONS]
  },
    {provide: MAT_DATE_FORMATS, useValue: DATE_FORMAT},
  ],
})
export class KillEditComponent {

  //Whether error messages should show up or not
  buttonClicked = false;

  //The outputs from the selectors
  selectedVictimBiteCode!: string | null;
  selectedKillerID!: number| null;

  //Takes in a kill object
  constructor(public dialogRef: MatDialogRef<KillEditComponent>,
              @Inject(MAT_DIALOG_DATA) public data:
                {victimName: string | null,
                  story: string | null,
                  timeOfDeath: string | null,
                  killerName: string | null,
                  ids: {name: string, id: number}[],
                  biteCodes: {name: string, biteCode: string}[]
                }) {
    //If there is a kill to be edited, these will have values, otherwise they will be null, so default values are set.
    if (data.victimName != null) this.selectedVictimBiteCode = data.biteCodes.find(v => v.name === data.victimName)!.biteCode;
    else this.selectedVictimBiteCode = data.biteCodes[0].biteCode
    if (data.killerName != null) this.selectedKillerID = data.ids.find(k => k.name === data.killerName)!.id;
    else this.selectedKillerID = data.ids[0].id
  }

  //Setters for the killerID and VictimBiteCode.
  selectVictim(event: Event) {
    this.selectedVictimBiteCode = (event.target as HTMLSelectElement).value;
  }
  selectKiller(event: Event) {
    this.selectedKillerID = parseInt((event.target as HTMLSelectElement).value);
  }

  /**
   * Is run whenever the save or delete buttons are clicked. It runs the data through some formatting and tests to see if the input is correct.
   * @param edit Is true if a kill should be edited or created. If it's false, the delete button has been clicked
   */
  closeDialog(edit: boolean) {
    if (edit) {
      this.buttonClicked = true;
      this.data.victimName = this.selectedVictimBiteCode;
      this.data.killerName = this.selectedKillerID!.toString();
      this.data.timeOfDeath = JSON.stringify(this.data.timeOfDeath).split("\"")[1];
      if (this.data.victimName != undefined && this.data.victimName.length > 0) {
        if (this.data.killerName != undefined && this.data.killerName.length > 0) {
          if (this.data.timeOfDeath != undefined && this.data.timeOfDeath.length > 0) {
            this.dialogRef.close(this.data);
          }
        }
      }
    }
    else {
      this.dialogRef.close(false);
    }
  }

}
