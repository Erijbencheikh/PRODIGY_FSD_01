import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthenticationService } from '../../Services/authentication.service';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-forgotpassword',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule,RouterLink],
  templateUrl: './forgotpassword.component.html',
  styleUrl: './forgotpassword.component.css'
})
export class ForgotpasswordComponent {
  forgetform : FormGroup
  constructor(private authservice: AuthenticationService, private formbuilder: FormBuilder, private route: Router){}
  ngOnInit(): void {
     this.forgetform = this.formbuilder.group({
      email:['', Validators.required]
     })
  }
  forgetPassword(){
    this.authservice.forgetPassword(this.forgetform.value.email).subscribe((res)=>{
      console.log(res);
       this.route.navigateByUrl('/gmailpassword')
    })
  }
}
