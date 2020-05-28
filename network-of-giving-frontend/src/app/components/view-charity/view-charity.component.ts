import { Component, OnInit } from '@angular/core';
import { CharityService } from 'src/app/services/charity.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-view-charity',
  templateUrl: './view-charity.component.html',
  styleUrls: ['./view-charity.component.css']
})
export class ViewCharityComponent implements OnInit {
  public charity;

  constructor(private charityService: CharityService, private route: ActivatedRoute,
              private primaryRoute: Router) { }

  ngOnInit(): void {
    this.getCharity(this.route.snapshot.params.id)
  }

  getCharity(title: string){
    this.charityService.getCharity(title).subscribe(
      data => {
        this.charity = data;  
      },
      err => console.log(err),
      () => console.log('charity is loaded')
    )
  }

  onBack(){
    this.primaryRoute.navigate(['/']);
  }

}
