import { Component, OnInit, OnDestroy, HostListener } from '@angular/core';
import { CharityService } from 'src/app/services/charity.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Charity } from 'src/app/models/charity';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { User } from 'src/app/models/user';
import { ModalComponent } from '../modal/modal.component';
import { Subscription, Observable } from 'rxjs';
import { take, mergeMap } from 'rxjs/operators';

@Component({
  selector: 'app-view-charity',
  templateUrl: './view-charity.component.html',
  styleUrls: ['./view-charity.component.css']
})
export class ViewCharityComponent implements OnInit, OnDestroy {
  public charity: Charity;
  public currentUser: User;
  private subscription: Subscription;

  //for participation
  volunteerModal=false;
  showAlertMessage=false;
  showSuccessMessage=false;

  //for donation
  donateModal=false;
  suggestionMoney=0;
  donatedMoney:number;
  successMessage:string;
  alertMessage: string;

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
  
  getCharity(title: string) {
    this.charityService.getCharity(title).pipe(mergeMap(
      data => {
        this.charity = data;
        return this.charityService.suggestMoneyToDonate(this.charity, this.currentUser.id);
      }
      )
      ).subscribe(
      data => {
        this.suggestionMoney = data;
        this.donatedMoney = data;
      },
      err => console.log(err)
    )
  }

  volunteer(){
    this.volunteerModal=false;
    this.charityService.participateInCharity(this.charity,this.currentUser.id).subscribe(
      data => {
        this.ngOnInit()
         this.showSuccessMessage=true;
         this.successMessage="You have successfully participated in the charity!";
      },
      err => {
         this.showAlertMessage=true;
         this.showSuccessMessage=false;
         this.alertMessage="No place for new volunteers or already participant"
      },
      () => {
        this.volunteerModal = false;
        
      }
    )
  }

  volunteerPopUp(){
    this.volunteerModal=true;
  }

  dismiss(){
    this.volunteerModal=false;
    this.donateModal=false;
    
  }
  donate(){
    const maxDonation = this.charity.budgetRequired - this.charity.amountCollected;
    if(this.suggestionMoney == 0) {
      //show alert
      return
    }
    if(this.donatedMoney >= 0 && this.donatedMoney <= maxDonation){
      this.charityService.donateMoneyInCharity(this.charity, this.donatedMoney,this.currentUser.id).subscribe(
        data => {
          this.ngOnInit()
          this.showSuccessMessage=true;
          this.successMessage=`You have successfully donated ${this.donatedMoney} in the charity!`;
        },
        err => {
          this.showAlertMessage=true;
          this.showSuccessMessage=false;
          this.alertMessage="Error in donation";
       }
   
      )  
    }
    else {
         this.showAlertMessage=true;
          this.showSuccessMessage=false;
          this.alertMessage="Your amount of money is incorrect. Please enter again!";
    }
    this.dismiss();
  }

  donatePopUp(){
    this.donateModal=true;
  }

}
