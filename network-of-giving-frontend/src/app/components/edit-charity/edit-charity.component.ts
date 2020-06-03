import { Component, OnInit } from '@angular/core';
import { CharityService } from 'src/app/services/charity.service';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from 'src/app/models/user';
import { Charity } from 'src/app/models/charity';
@Component({
  selector: 'app-edit-charity',
  templateUrl: './edit-charity.component.html',
  styleUrls: ['./edit-charity.component.css']
})
export class EditCharityComponent implements OnInit {
  currentUser: User;
  charity: Charity;
  selectedFile: File;
  currentCharityTitle: string;
  constructor(private charityService: CharityService,
    private route: ActivatedRoute,
    private router: Router,
    private auth: AuthenticationService) { 
      this.currentUser = auth.currentUserValue
  }
  ngOnInit(): void {
    this.currentCharityTitle = this.route.snapshot.params.id;
    this.getCharity(this.currentCharityTitle);
  }
  getCharity(title){
    this.charityService.getCharity(title).subscribe(
      data => {
        this.charity = data
      },
      err => {
        console.log(err);
      }
    )
  }
  editCharity(){
    this.charityService.updateCharity(this.charity, this.currentCharityTitle).subscribe(
      data => {
        //go to view-page
        this.router.navigate([`/charity/view/${this.charity.title}`])
        return true;
      },
      err => {
        // show error message
        console.log(err)
      }
    )
  }
  
}