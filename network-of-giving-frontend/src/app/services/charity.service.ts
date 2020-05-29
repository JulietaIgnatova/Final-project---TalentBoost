import { Injectable } from '@angular/core';

import { HttpClient, HttpHeaders } from '@angular/common/http'
import { Observable } from 'rxjs';
import { of } from 'rxjs';

@Injectable()
export class CharityService {

  constructor(private http: HttpClient) { }

  getCharities(){
    return this.http.get('http://localhost:8080/api/v1/charities/');
  }

  getCharity(title: string){
    console.log(title);
    return this.http.get('http://localhost:8080/api/v1/charities/' + title);
  }
  
  getFilteredCharity(title: string){
    console.log(title);
     return this.http.get('http://localhost:8080/api/v1/charities/filtered?title=' + title);
  }

 
  createCharityWithImage(charityFormData){
    let headers = new HttpHeaders();
    headers = headers.append("Authorization", "Basic " + btoa('mmitkoo' + ':' + '123456'));
    return this.http.post<any>('http://localhost:8080/api/v1/charities/',charityFormData, {headers});
  }

}
