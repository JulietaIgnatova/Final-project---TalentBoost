import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subject, Subscription } from 'rxjs';

type OK_CANCEL = 'OK'|'Cancel';

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.scss']
})
export class ModalComponent implements OnInit, OnDestroy {
  static openClose = new Subject<boolean>();
  static onBtn = new Subject<OK_CANCEL>();
  open = false;
  private subscription: Subscription;
  constructor() { 
    
  }

  ngOnInit(): void {
    this.subscription = ModalComponent.openClose.subscribe(v => this.open = v);
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  onBtn(btn: OK_CANCEL) {
    ModalComponent.onBtn.next(btn);
  }
}
