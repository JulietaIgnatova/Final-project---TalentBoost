import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateCharityComponent } from './create-charity.component';

describe('CreateCharityComponent', () => {
  let component: CreateCharityComponent;
  let fixture: ComponentFixture<CreateCharityComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CreateCharityComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateCharityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
