import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http'
import { Observable, BehaviorSubject } from 'rxjs';
import { of } from 'rxjs';
import { Charity } from '../models/charity';
import { environment } from 'src/environments/environment';

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
    return this.http.post<any>('http://localhost:8080/api/v1/charities/',charityFormData);
  }

  deleteCharity(title: string): Observable<boolean> {
     return this.http.delete<any>(`${environment.apiUrl}/charities/${title}`);
  }


}
