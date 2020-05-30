import { Component, OnInit } from '@angular/core';
import { CharityService } from 'src/app/services/charity.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Charity } from 'src/app/models/charity';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { User } from 'src/app/models/user';

@Component({
  selector: 'app-view-charity',
  templateUrl: './view-charity.component.html',
  styleUrls: ['./view-charity.component.css']
})
export class ViewCharityComponent implements OnInit {
  public charity: Charity;
  public currentUser: User;

  constructor(
     private charityService: CharityService,
     private route: ActivatedRoute,
     private primaryRoute: Router,
     private auth: AuthenticationService,
     ) { 
      this.currentUser = auth.currentUserValue;
     }

  ngOnInit(): void {
    this.getCharity(this.route.snapshot.params.id)
  }

  getCharity(title: string){
    this.charityService.getCharity(title).subscribe(
      data => {
        this.charity = data;  
      },
      err => console.log(err),
      () => console.log('charity is loaded')
    )
  }

}
