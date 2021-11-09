import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GameInfoPage } from './game-info.page';

describe('GameInfoPage.Page.TsComponent', () => {
  let component: GameInfoPage;
  let fixture: ComponentFixture<GameInfoPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GameInfoPage ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GameInfoPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
