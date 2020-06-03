import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditCharityComponent } from './edit-charity.component';

describe('EditCharityComponent', () => {
  let component: EditCharityComponent;
  let fixture: ComponentFixture<EditCharityComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditCharityComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditCharityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
