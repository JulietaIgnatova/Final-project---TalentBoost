import { Injectable } from '@angular/core';

import { HttpClient, HttpHandler, HttpHeaders } from '@angular/common/http'
import { Observable, BehaviorSubject } from 'rxjs';
import { of } from 'rxjs';
import { Charity } from '../models/charity';
import { environment } from 'src/environments/environment';

const headerDict = {
  'Content-Type': 'application/json'
};

const requestOptions = {
  headers: new HttpHeaders(headerDict)
};

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
    return this.http.get<Charity[]>(`${environment.apiUrl}/charities/`);
  }

  getCharity(title: string):Observable<Charity>{
    console.log(title);
    return this.http.get<Charity>(`${environment.apiUrl}/charities/${title}`);
  }
  
  getFilteredCharity(title: string):Observable<Charity[]>{
    console.log(title);
     return this.http.get<Charity[]>(`${environment.apiUrl}/charities/filtered?title=${title}`);
  }

 
  createCharityWithImage(charityFormData){
    
    return this.http.post<any>(`${environment.apiUrl}/charities/`,charityFormData);
  }

  deleteCharity(title: string): Observable<boolean> {
     return this.http.delete<any>(`${environment.apiUrl}/charities/${title}`);
  }

  participateInCharity(charity: Charity, userId: number): Observable<boolean> {
   let body = JSON.stringify(charity); 
   return this.http.post<any>(`${environment.apiUrl}/charities/participate/${userId}`, body,requestOptions);
 }

 donateMoneyInCharity(charity: Charity, money:number, userdId: number){
  let body = JSON.stringify(charity);
  return this.http.post<any>(`${environment.apiUrl}/charities/donate/${userdId}?money=${money}`, body, requestOptions);
} 

   suggestMoneyToDonate(charity: Charity, userId: number): Observable<number>{
    let body = JSON.stringify(charity); 
    return this.http.post<number>(`${environment.apiUrl}/charities/suggestion/${userId}`, body,requestOptions);
   }

}
