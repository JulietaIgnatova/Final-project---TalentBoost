import { Component, OnInit } from '@angular/core';

import {CharityService } from '../../services/charity.service'
import { Router } from '@angular/router';
import { AuthenticationService} from 'src/app/services/authentication.service'
import { User } from 'src/app/models/user'
import { SearchService } from 'src/app/services/search.service';

@Component({
  selector: 'app-charity',
  templateUrl: './charity.component.html',
  styleUrls: ['./charity.component.css']
})
export class CharityComponent implements OnInit {
  public charities;
  public titleToSearch: string;
  public currentUser: User;
 


  constructor(private charityService: CharityService,
    private router: Router, private auth: AuthenticationService,
    private searchService: SearchService) {
      this.currentUser = auth.currentUserValue;
    }

    ngOnInit(): void {
      if(this.charities == null) {
        this.charityService.getCharities().subscribe(
          data => {
            this.charities = data
          }
        )
      }
        this.searchService.onResults().subscribe(
          data => {
            this.charities = data
          }
        )
    }

  

  

} 
