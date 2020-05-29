import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { UserService } from 'src/app/services/user.service';
import { Observable } from 'rxjs';


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
   registerForm: FormGroup
  constructor(private router: Router, private userService: UserService) { }

  ngOnInit(): void {
    this.registerForm = new FormGroup({
      username: new FormControl('',Validators.required),
      password: new FormControl('',Validators.required),
      name: new FormControl('',Validators.required),
      age: new FormControl('',Validators.required),
      gender: new FormControl(''),
      location: new FormControl('')
    })
  }

  register() {
    if(this.registerForm.valid){
      var selector = document.querySelector('input[name="options"]:checked');
      if(selector){
        var genderValue = (<HTMLInputElement>selector).value
        this.registerForm.patchValue({gender: genderValue})
      }

      console.log(this.registerForm.value) //for debugging

      this.userService.registerUser(this.registerForm).subscribe(
        date => {
          this.registerForm.reset();
          (<HTMLInputElement>selector).checked = false
          console.log("User registered successful")
          this.router.navigate(['/login'])
          return true;
        },
        error => {
          return Observable.throw(error)
        }
      )

    }
  }

  onLogin(){
    this.router.navigate(['/login'])
  }
}
