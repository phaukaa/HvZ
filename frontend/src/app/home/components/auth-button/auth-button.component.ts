import {Component, EventEmitter, Inject, Input, OnInit, Output} from '@angular/core';
import { AuthService } from '@auth0/auth0-angular';
import { DOCUMENT } from '@angular/common';
import {HomeAPI} from "../../api/home.api";
import {UserPlayer} from "../../../models/input/user-player.model";

@Component({
  selector: 'app-auth-button',
  templateUrl: './auth-button.component.html',
  styleUrls: ['./auth-button.component.css']
})
export class AuthButtonComponent implements OnInit {
  getRequestSent = false;
  postRequestSent = false;

  constructor(@Inject(DOCUMENT) public document: Document, public auth: AuthService, private readonly homeAPI: HomeAPI) { }

  players: UserPlayer[] = [];
  @Output()
  playerChange = new EventEmitter<{admin: boolean, players: UserPlayer[]}>();
  @Input()
  public isMobile!: boolean;

  ngOnInit(): void {
    this.auth.idTokenClaims$.subscribe((token) => {
      if (token != null) {
        if (!this.getRequestSent) { //Ensures only one request is sent
          this.getRequestSent = true;
          this.homeAPI.checkUser()
            .then(res => {
              res.subscribe(
                data => {
                  this.playerChange.emit({admin: data.admin, players: data.players});
                }, //If the user is found
                err => {
                  if (err.status == 404) {//If user doesn't exist
                    if (!this.postRequestSent) { //Ensures only one request is sent
                      this.postRequestSent = true;
                      let firstName = token.given_name;
                      let lastName = token.family_name;
                      if (firstName == undefined || lastName == undefined) {
                        firstName = token.email?.split('@')[0];
                        lastName = '';
                      }
                      this.homeAPI.createUser({firstName: firstName, lastName: lastName})
                        .then(res => {res.subscribe(data => data)});
                    }
                  }
                });
            });
        }
      }
    });
  }
}
