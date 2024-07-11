import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthenticationService } from '../../Services/authentication.service';
import Validation from '../../../../helper/validation';


@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [ReactiveFormsModule,CommonModule,RouterLink],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css'
})
export class SignupComponent {
  signupform : FormGroup
  submitted = false;
  constructor(private signupService : AuthenticationService, private formbuiler: FormBuilder, private router: Router){}
  ngOnInit(): void {
   this.signupform = this.formbuiler.group({
    username: ['',
      Validators.required,
      Validators.minLength(6),
      Validators.maxLength(25)
    ],
    password : ['',
      Validators.required,
      Validators.minLength(6),
      Validators.maxLength(40)
    ],
    email:['', Validators.required, Validators.email],
    role:['', Validators.required],
    confirmPassword: ['', Validators.required],
    acceptTerms: [false, Validators.requiredTrue]
   },{
    validators: [Validation.match('password', 'confirmPassword')],
  }
   );
  }
  get f(){
    return this.signupform.controls;
  }


  onSubmit(): void {
    this.submitted = true;
    if (this.signupform.invalid){
      return
    }
    this.signupform.patchValue({
      role:["admin"]
    })

    this.signupService.signup(this.signupform.value).subscribe((res:any)=>{
      console.log("Account :",res);
      this.router.navigateByUrl('/home')
    })
  }
}
