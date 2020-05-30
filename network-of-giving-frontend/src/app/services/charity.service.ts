import { Injectable } from '@angular/core';

import { HttpClient, HttpHeaders } from '@angular/common/http'
import { Observable, BehaviorSubject } from 'rxjs';
import { of } from 'rxjs';
import { Charity } from '../models/charity';

@Injectable()
export class CharityService {

  private currentCharities: BehaviorSubject<Charity[]>;
  

  public get currentAvailableCharities(): Charity[] {
    return this.currentCharities.value;
}

  constructor(private http: HttpClient) { 
    this.currentCharities = new BehaviorSubject<Charity[]>([]);
       
  }

  getCharities(): Observable<Charity[]>{
    return this.http.get<Charity[]>('http://localhost:8080/api/v1/charities/');
  }

  getCharity(title: string):Observable<Charity>{
    console.log(title);
    return this.http.get<Charity>('http://localhost:8080/api/v1/charities/' + title);
  }
  
  getFilteredCharity(title: string):Observable<Charity[]>{
    console.log(title);
     return this.http.get<Charity[]>('http://localhost:8080/api/v1/charities/filtered?title=' + title);
  }

 
  createCharityWithImage(charityFormData){
    let headers = new HttpHeaders();
    headers = headers.append("Authorization", "Basic " + btoa('mmitkoo' + ':' + '123456'));
    return this.http.post<any>('http://localhost:8080/api/v1/charities/',charityFormData, {headers});
  }

}
