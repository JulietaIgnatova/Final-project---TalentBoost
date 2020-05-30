import { Component, OnInit, OnDestroy, HostListener } from '@angular/core';
import { CharityService } from 'src/app/services/charity.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Charity } from 'src/app/models/charity';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { User } from 'src/app/models/user';
import { ModalComponent } from '../modal/modal.component';
import { Subscription, Observable } from 'rxjs';
import { take } from 'rxjs/operators';

@Component({
  selector: 'app-view-charity',
  templateUrl: './view-charity.component.html',
  styleUrls: ['./view-charity.component.css']
})
export class ViewCharityComponent implements OnInit, OnDestroy {
  public charity: Charity;
  public currentUser: User;
  private subscription: Subscription;

  constructor(
     private charityService: CharityService,
     private route: ActivatedRoute,
     private router: Router,
     private auth: AuthenticationService,
     ) { 
      this.currentUser = auth.currentUserValue;
     }

  ngOnInit(): void {
    this.getCharity(this.route.snapshot.params.id)
  }

  ngOnDestroy(): void {
    if(this.subscription) {
      this.subscription.unsubscribe();
    }
  }
  
  onDelete(title: string): void {
    ModalComponent.openClose.next(true);
    this.subscription = ModalComponent.onBtn.pipe(take(1))
      .subscribe(v => {
        v === 'OK' && this.charityService.deleteCharity(title).subscribe(
          res => {
            this.router.navigate(['/']);
            return true;
          },
          err => {
            console.log(Observable.throw(err))
            return false;
          }
        )
      });
  }

  @HostListener('click', ['$event'])
  onClick($event: MouseEvent) {
    $event.stopPropagation();
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
}
