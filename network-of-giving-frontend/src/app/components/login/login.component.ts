import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { first } from 'rxjs/operators';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup
  returnUrl: string;
  invalidLogin = false;

  constructor(private router: Router, private auth: AuthenticationService,private route: ActivatedRoute ){ 
    if (this.auth.currentUserValue) { 
      this.router.navigate(['/']);
  }
  }

  ngOnInit(): void {
    this.loginForm = new FormGroup({
      username: new FormControl('',Validators.required),
      password: new FormControl('',Validators.required),
    })
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }
 
  submitLogin(){
    if (this.loginForm.invalid) {
          return;
     }
    

     this.auth.login(this.loginForm.get("username").value, this.loginForm.get("password").value)
     .pipe(first())
     .subscribe(
         data => {
             this.router.navigate([this.returnUrl]);
         },
         error => {
          this.invalidLogin=true;
             console.log(error);
         });
  }

  onRegister(){
    this.router.navigate(['/register'])
  }

}
