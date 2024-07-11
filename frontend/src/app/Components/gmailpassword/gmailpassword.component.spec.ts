import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GmailpasswordComponent } from './gmailpassword.component';

describe('GmailpasswordComponent', () => {
  let component: GmailpasswordComponent;
  let fixture: ComponentFixture<GmailpasswordComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GmailpasswordComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GmailpasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
