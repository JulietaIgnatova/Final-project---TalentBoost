import { Component, OnInit } from '@angular/core';
import { CharityService } from 'src/app/services/charity.service';
import { UserService } from 'src/app/services/user.service';
import { Charity } from 'src/app/models/charity';
import { User } from 'src/app/models/user';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  public charity: Charity;
  public user: User;

  constructor(private charityService: CharityService, private userService: UserService) { }

  ngOnInit(): void {
  }

}
