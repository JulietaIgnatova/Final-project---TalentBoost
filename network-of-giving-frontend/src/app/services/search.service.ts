import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Charity } from '../models/charity';
import { CharityService } from './charity.service';

@Injectable({
  providedIn: 'root'
})
export class SearchService {
  private currentCharities: BehaviorSubject<Charity[]>;
  constructor(private charityService: CharityService) { 
    this.currentCharities = new BehaviorSubject<Charity[]>([]);
  }
  public get currentAvailableCharities(): Charity[] {
    return this.currentCharities.value;
  }
  onResults() {
    return this.currentCharities.asObservable();
  }
  search(title: string) {
    this.charityService.getFilteredCharity(title)
    .subscribe(results => this.currentCharities.next(results));
  }
}