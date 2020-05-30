import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CharityComponent } from './components/charity/charity.component';

import {CreateCharityComponent} from './components/create-charity/create-charity.component'
import { RegisterComponent } from './components/register/register.component';
import { LoginComponent } from './components/login/login.component';
import { ViewCharityComponent } from './components/view-charity/view-charity.component';
import { ProfileComponent } from './components/profile/profile.component';


const routes: Routes = [
  {
    path: "",
    component: CharityComponent
  },
  {
    path:"login",
    component: LoginComponent
  },
  {
    path:"register",
    component: RegisterComponent
  },
  {
    path:"charity/view/:id",
    component: ViewCharityComponent
  },
  {
    path: "createCharity",
    component: CreateCharityComponent
  },
  {
    path: "profile",
    component: ProfileComponent
  },
  
]

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
