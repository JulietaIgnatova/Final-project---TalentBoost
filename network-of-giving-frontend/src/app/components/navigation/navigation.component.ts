import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { CharityService } from 'src/app/services/charity.service';
import { Charity } from 'src/app/models/charity';
import { User } from 'src/app/models/user';
import { SearchService } from 'src/app/services/search.service';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavigationComponent implements OnInit {
  currentUser: User;
  titleToSearch: string;
  charities: Charity[]
  logoutModal=false;


  constructor(public router: Router,
    private auth: AuthenticationService,
    private charityService: CharityService, private searchService: SearchService) { 
      this.auth.currentUser.subscribe(x => this.currentUser = x);
  }
  ngOnInit(): void {
  }

  logout() {
     this.logoutModal=false;
    this.auth.logout();
     this.router.navigate(['/']);
  }

  searchForCharity(){
    this.searchService.search(this.titleToSearch);
  }

  logoutPopUp(){
    this.logoutModal=true;
  }

  public cancel(): void {
    this.logoutModal = false;
  }
}