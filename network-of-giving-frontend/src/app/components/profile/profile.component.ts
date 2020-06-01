import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { User } from 'src/app/models/user';
import { UserService } from 'src/app/services/user.service';
import { Charity } from 'src/app/models/charity';
import { mergeMap } from 'rxjs/operators';
import { UserAction } from 'src/app/models/useraction';
@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  currentUser: User;
  allParticipatedCharities: Charity[];
  allDonatedCharities: Charity[];
  allCreatedCharities: Charity[];
  latestUserActions: UserAction[];
  
  constructor(private userService: UserService,
    private auth: AuthenticationService) {
      this.currentUser = auth.currentUserValue
  }
  ngOnInit(): void {
    this.getAllNeededCharities();
  }
  getAllNeededCharities() {
    this.userService.getAllParticipatedCharity(this.currentUser.username).pipe(mergeMap(
        data => {
          this.allParticipatedCharities = data;
          return this.userService.getAllDonatedCharity(this.currentUser.username);
        }
      )
    ).pipe(mergeMap(
      data => {
        this.allDonatedCharities = data;
        return this.userService.getAllCreatedCharity(this.currentUser.username)
      }
    )
    ).pipe(mergeMap(
      data => {
        this.allCreatedCharities = data
        return this.userService.getLatestActivitiesForUser(this.currentUser.id)
      }
    )
    ).subscribe(
      data => {
        this.latestUserActions = data
      },
      err => {
        console.log(err)
      }
    )
  }
}