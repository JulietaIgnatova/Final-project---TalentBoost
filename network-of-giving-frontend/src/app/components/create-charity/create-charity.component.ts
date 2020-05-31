import { Component, OnInit } from '@angular/core';
import {CharityService } from '../../services/charity.service';
import {FormGroup, FormControl, Validators} from '@angular/forms';
import { Observable } from 'rxjs';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { User } from 'src/app/models/user';
import { Router } from '@angular/router';
import { Charity } from 'src/app/models/charity';


@Component({
  selector: 'app-create-charity',
  templateUrl: './create-charity.component.html',
  styleUrls: ['./create-charity.component.css']
})
export class CreateCharityComponent implements OnInit {
  charityForm: FormGroup;
  validMessage: string = "";
  selectedFile: File;
  public currentUser: User;
  charityCreateModal = false;
  showAlert=false;

  constructor(
    private charityService: CharityService,
    private auth: AuthenticationService,
    private router: Router) {
    this.currentUser = auth.currentUserValue;
    }

  ngOnInit(): void {
    this.charityForm = new FormGroup({
      creatorId: new FormControl(''),
      title: new FormControl('',Validators.required),
      description: new FormControl('',Validators.required),
      budgetRequired: new FormControl(''),
      volunteersRequired: new FormControl('')
    })
  }

  submitCharity() {
    
    if(this.charityForm.valid){
      
      this.validMessage = "Your charity form has been submitted. Thank you!"
      const submitFormData = new FormData;
      submitFormData.append('imageFile', this.selectedFile);
      this.charityForm.patchValue({creatorId: this.currentUser.id});
      let body = JSON.stringify(this.charityForm.value);
      submitFormData.append('body',body);
    
      this.charityService.createCharityWithImage(submitFormData).subscribe(
        date => {
          this.charityForm.reset();
          this.selectedFile = null;
          this.charityCreateModal=true;
          this.showAlert=false;
          return true;
        },
        error => {
          this.showAlert=true;
          return Observable.throw(error);
        }
      )
      }
  }

  processFile(imageInput: any){
    this.selectedFile = imageInput.target.files[0];
  }

  public ok(): void {
    this.charityCreateModal = false;
  }
  public cancel(): void {
    this.charityCreateModal = false;
    this.router.navigate(['/']);
  }

  checkTitle(title : string):boolean{
    var charities : Charity[]
     this.charityService.getCharities().subscribe(
      data =>{
        charities=data;
       }
     )
     if(charities.filter(function(charity) {
       return charity.title == title;
     }).length == 1){
       return true;
     }
     return false;
  }

}
