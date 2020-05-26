import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewCharityComponent } from './view-charity.component';

describe('ViewCharityComponent', () => {
  let component: ViewCharityComponent;
  let fixture: ComponentFixture<ViewCharityComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ViewCharityComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewCharityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
