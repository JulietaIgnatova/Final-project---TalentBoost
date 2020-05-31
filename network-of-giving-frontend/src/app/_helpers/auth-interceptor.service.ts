import {Injectable} from '@angular/core';
import {UserService} from "../services/user.service";
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";
import { AuthenticationService } from '../services/authentication.service';

@Injectable({
    providedIn: 'root'
})
export class AuthInterceptor implements HttpInterceptor {


    constructor(private auth: AuthenticationService) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        // add authorization header with jwt token if available
        const currentUser = this.auth.currentUserValue;
        if (currentUser) {
            request = request.clone({
                headers: 
                    request.headers.append(`Authorization`, `Basic ${localStorage.getItem('token')}`)
            });
        }
        return next.handle(request);
    }
  }

