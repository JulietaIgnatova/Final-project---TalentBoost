import { Component, OnInit } from '@angular/core';

import {CharityService } from '../../services/charity.service'
import { Router } from '@angular/router';

@Component({
  selector: 'app-charity',
  templateUrl: './charity.component.html',
  styleUrls: ['./charity.component.css']
})
export class CharityComponent implements OnInit {
  public charities;
  public titleToSearch: string;

  constructor(private charityService: CharityService,
    private router: Router) { }

  ngOnInit(): void {
    this.getCharities();
  }

  getCharities(){
    this.charityService.getCharities().subscribe(
      data => {this.charities = data},
      err => console.error(err),
      () => console.log('charities loaded')
    
    )
  }

  searchForCharity(){
    if(this.titleToSearch == ""){
      this.getCharities();
      return;
    }
    this.charityService.getFilteredCharity(this.titleToSearch).subscribe(
      data => {
        this.charities = data
      },
      err => console.log(err),
      () => console.log('charity found')
    );
  }

  onLogin(){
    this.router.navigate(['/login'])
  }
  
  onRegister(){
    this.router.navigate(['/register'])
  }


} 
