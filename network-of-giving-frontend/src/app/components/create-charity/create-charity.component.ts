import { Component, OnInit } from '@angular/core';
import {CharityService } from '../../services/charity.service';
import {FormGroup, FormControl, Validators} from '@angular/forms';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-create-charity',
  templateUrl: './create-charity.component.html',
  styleUrls: ['./create-charity.component.css']
})
export class CreateCharityComponent implements OnInit {
  charityForm: FormGroup;
  validMessage: string = "";

  constructor(private charityService: CharityService) { }

  ngOnInit(): void {
    this.charityForm = new FormGroup({
      creatorId: new FormControl('',Validators.required),
      title: new FormControl('',Validators.required),
      description: new FormControl('',Validators.required),
      budgetRequired: new FormControl('',Validators.required),
      volunteersRequired: new FormControl('',Validators.required)
    })
  }

  submitCharity() {
    if(this.charityForm.valid){
      this.validMessage = "Your charity form has been submitted. Thank you!"
      this.charityService.createCharity(this.charityForm.value).subscribe(
        date => {
          this.charityForm.reset();
          return true;
        },
        error => {
          return Observable.throw(error)
        }
      )
    } else {
      this.validMessage = "Please fill out the form before submitting!"
    }
  }

}
