import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { HttpClientModule,HttpClientJsonpModule } from '@angular/common/http'
import { AppComponent } from './app.component';
import { ClarityModule } from '@clr/angular';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CharityComponent } from './components/charity/charity.component';

import { CharityService } from './services/charity.service';
import { AppRoutingModule } from './app-routing.module';
import { CreateCharityComponent } from './components/create-charity/create-charity.component'
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RegisterComponent } from './components/register/register.component';
import { LoginComponent } from './components/login/login.component';
import { ViewCharityComponent } from './components/view-charity/view-charity.component';

import { NavigationComponent } from './components/navigation/navigation.component';
import { ProfileComponent } from './components/profile/profile.component';
import { ShareButtonsModule } from '@ngx-share/buttons';


@NgModule({
  declarations: [
    AppComponent,
    CharityComponent,
    CreateCharityComponent,
    RegisterComponent,
    LoginComponent,
    ViewCharityComponent,
 
    NavigationComponent,
 
    ProfileComponent
  ],
  imports: [
    BrowserModule,
    ClarityModule,
    BrowserAnimationsModule,
    HttpClientModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientJsonpModule,
    ShareButtonsModule.withConfig({
      debug: true
    })
  ],
  providers: [CharityService],
  bootstrap: [AppComponent]
})
export class AppModule { }
