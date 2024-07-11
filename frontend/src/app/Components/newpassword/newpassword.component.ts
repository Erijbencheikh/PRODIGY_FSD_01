import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthenticationService } from '../../Services/authentication.service';
import { CommonModule } from '@angular/common';
import Validation from '../../../../helper/validation';

@Component({
  selector: 'app-newpassword',
  standalone: true,
  imports: [ReactiveFormsModule,CommonModule,RouterLink],
  templateUrl: './newpassword.component.html',
  styleUrl: './newpassword.component.css'
})
export class NewpasswordComponent {
  resetform : FormGroup
  submitted = false;

  restToken = this.activateroute.snapshot.params['resettoken']
constructor(private formbuilder : FormBuilder, private activateroute: ActivatedRoute, private authservice:AuthenticationService, private route:Router){}
ngOnInit(): void {
  this.resetform = this.formbuilder.group({
    newPassword : ['',  [
      Validators.required,
      Validators.minLength(6),
      Validators.maxLength(40)
    ]],
    confirmPassword: ['', Validators.required],
  },
  {
    validators: [Validation.match('newPassword', 'confirmPassword')]
  })

}
get f(){
  return this.resetform.controls;
}


onSubmit(): void {
  this.submitted = true;

  if (this.resetform.invalid) {
    return;
  }
  this.authservice.resetPassword(this.restToken, this.resetform.value.newPassword).subscribe((res:any)=>{
    console.log(res);
    this.route.navigateByUrl('/login')
  })
}
}
