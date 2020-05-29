import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http'

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  registerUser(userForm){
    let headers = new HttpHeaders();
    headers = headers.append("Content-Type", "application/json");

    let body = JSON.stringify(userForm.value);
    
    return this.http.post('http://localhost:8080/api/v1/users/',body, {headers})
  }
}
