import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators,ReactiveFormsModule, FormControl } from '@angular/forms';
import Swal from 'sweetalert2';
import { AuthenticationService } from '../../Services/authentication.service';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule,RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  formlogin: FormGroup;
  submitted = false;

  constructor(
    private loginservice: AuthenticationService,
    private formbuilder: FormBuilder,
    private router:Router
  ){}
  ngOnInit(): void {
    this.formlogin = this.formbuilder.group({
      password: [
        '',
        [
          Validators.required,
          Validators.minLength(6),
          Validators.maxLength(40)
        ]
      ],
      username: [
        '',
        [
          Validators.required,
          Validators.minLength(6),
          Validators.maxLength(20),
        ],
      ],
    });
  }
  onSubmit(): void {
    this.submitted = true;

    if (this.formlogin.invalid) {
      return;
    }
    this.loginservice.login(this.formlogin.value).subscribe(
      (res: any) => {
        console.log('Login', res);
        if (res.enabled == true) {
          localStorage.setItem('userconnect', JSON.stringify(res));
          localStorage.setItem('token', res.accessToken);
          localStorage.setItem('state', '0');
          this.router.navigateByUrl('/home');
        }
      },
      (err) => {
        Swal.fire({
          icon: 'error',
          title: 'User not found',
          footer: 'Or password invalid',
        });
        console.log(err);
      }
    );
  }
  get f() {
    return this.formlogin.controls;
  }
}
