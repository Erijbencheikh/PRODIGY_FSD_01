import { Routes } from '@angular/router';
import { LoginComponent } from './Components/login/login.component';
import { AppComponent } from './app.component';
import { SignupComponent } from './Components/signup/signup.component';
import { ForgotpasswordComponent } from './Components/forgotpassword/forgotpassword.component';
import { Component } from '@angular/core';
import { GmailpasswordComponent } from './Components/gmailpassword/gmailpassword.component';
import { NewpasswordComponent } from './Components/newpassword/newpassword.component';
import { AuthGuard } from './guard/auth.guard';
import { HomeComponent } from './Components/home/home.component';

export const routes: Routes = [
  {path:'', component:LoginComponent},
  {path:'signup', component:SignupComponent},
  {path:'forgetpassword',component:ForgotpasswordComponent},
  {path:'gmailpassword',component:GmailpasswordComponent},
  {path:'newpassword/:resettoken',component:NewpasswordComponent},
  {path:'home', canActivate:[AuthGuard], component:HomeComponent, children:[




  ]}

];
