import {Component, Inject} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-view-squad-admin',
  templateUrl: './viewsquad.component.html',
  styleUrls: ['./viewsquad.component.css']
})
export class ViewsquadComponent {

  constructor(public dialogRef: MatDialogRef<ViewsquadComponent>,
              @Inject(MAT_DIALOG_DATA) public data:
                {
                  id: number | null,
                  name: string | null,
                  members: any[] | null
                }) {}

}
