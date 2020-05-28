import { Injectable } from '@angular/core';

import { HttpClient, HttpHeaders } from '@angular/common/http'
import { Observable } from 'rxjs';
import { of } from 'rxjs';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
}

@Injectable()
export class CharityService {

  constructor(private http: HttpClient) { }

  getCharities(){
    return this.http.get('http://localhost:8080/api/v1/charities');
  }

  getCharity(title: string){
    console.log(title);
    return this.http.get('http://localhost:8080/api/v1/charities/' + title);
  }
  
  getFilteredCharity(title: string){
    console.log(title);
     return this.http.get('http://localhost:8080/api/v1/charities/filtered?title=' + title);
  }

  createCharity(charity){
    let body = JSON.stringify(charity);
    return this.http.post('http://localhost:8080/api/v1/charities/', body, httpOptions)
  }

  createCharityWithImage(charityFormData){
    return this.http.post<any>('http://localhost:8080/api/v1/charities/',charityFormData);
  }

}
