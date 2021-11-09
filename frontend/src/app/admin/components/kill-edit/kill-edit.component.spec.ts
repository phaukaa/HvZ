import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KillEditComponent } from './kill-edit.component';

describe('KillEditComponent', () => {
  let component: KillEditComponent;
  let fixture: ComponentFixture<KillEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ KillEditComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(KillEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
