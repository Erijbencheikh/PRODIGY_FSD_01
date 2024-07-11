import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, } from '@angular/forms';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {HttpClient,HttpClientModule}from '@angular/common/http';
import 'chartjs-adapter-moment';
import { RouterLinkActive, RouterOutlet, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';



@NgModule({
  declarations: [






  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    CommonModule, // add here if necessasry
    RouterLinkActive,// add here if necessasry
    RouterOutlet, // add here if necessasry
    RouterLink // add here if necessasry

  ],
  providers: [],
  bootstrap: []
})
export class AppModule { }
// AOT compilation support

