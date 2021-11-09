import {Injectable} from "@angular/core";
import {CanActivate} from "@angular/router";
import {AuthService} from "@auth0/auth0-angular";
import {Observable} from "rxjs";

@Injectable()
export class AuthGuard implements CanActivate {
  constructor(public auth: AuthService) {
  }

  //If injected in a module, it ensures that the user has logged in.
  canActivate(): Observable<boolean> {
    return this.auth.isAuthenticated$;
  }
}
