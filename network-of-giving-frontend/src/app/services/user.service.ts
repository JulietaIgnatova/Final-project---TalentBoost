import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http'
import {  Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { User } from '../models/user';
import { Charity } from '../models/charity';
import { UserAction } from '../models/useraction';
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
  getAllParticipatedCharity(username: string): Observable<Charity[]> {
    return this.http.get<any>(`${environment.apiUrl}/users/${username}/charities/participated`);
  }
  getAllDonatedCharity(username: string): Observable<Charity[]> {
    return this.http.get<any>(`${environment.apiUrl}/users/${username}/charities/donated`);
  }
  getAllCreatedCharity(username: string): Observable<Charity[]> {
    return this.http.get<any>(`${environment.apiUrl}/users/${username}/charities/created`);
  }

  getLatestActivitiesForUser(userId: number): Observable<UserAction[]> {
    return this.http.get<any>(`${environment.apiUrl}/actions/${userId}`);
  }
}