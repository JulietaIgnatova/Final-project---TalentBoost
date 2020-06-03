import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CharityComponent } from './components/charity/charity.component';
import { RegisterComponent } from './components/register/register.component';
import { LoginComponent } from './components/login/login.component';
import { ViewCharityComponent } from './components/view-charity/view-charity.component';
import { ProfileComponent } from './components/profile/profile.component';
import { AuthGuard } from './_helpers/auth.guard';
import { EditCharityComponent } from './components/edit-charity/edit-charity.component';
import { CreateCharityComponent } from './components/create-charity/create-charity.component';


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
    path:"charity/edit/:id",
    component: EditCharityComponent,
    canActivate: [AuthGuard]
  },
  {
    path: "createCharity",
    component: CreateCharityComponent,
    canActivate: [AuthGuard]
  },
  {
    path: "profile",
    component: ProfileComponent,
    canActivate: [AuthGuard]
  },
  
]

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
