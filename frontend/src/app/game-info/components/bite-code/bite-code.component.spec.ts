import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BiteCodeComponent } from './bite-code.component';

describe('BiteCodeComponent', () => {
  let component: BiteCodeComponent;
  let fixture: ComponentFixture<BiteCodeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BiteCodeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BiteCodeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
