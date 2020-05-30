import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http'

import { environment } from '../../environments/environment'
import { User } from '../models/user'

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  registerUser(userForm) {
    let headers = new HttpHeaders();
    headers = headers.append("Content-Type", "application/json");
    let body = JSON.stringify(userForm.value);
    return this.http.post(`${environment.apiUrl}/users/`, body, {headers})
  }
  
  getUser(username){
    return this.http.get<User>(`${environment.apiUrl}/users/${username}`);
  }
}
