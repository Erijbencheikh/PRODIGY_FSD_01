import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment.development';


@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(private http : HttpClient) { }
  login(loginrequest : any){
    return this.http.post(`${environment.baseurl}/api/auth/signin`,loginrequest)
  }
  signup(signuprequest: any){
    return this.http.post(`${environment.baseurl}/admin/signup`,signuprequest)
  }
  forgetPassword(email:any){
    return this.http.post(`${environment.baseurl}/api/auth/forgetpassword?email=${email}`,{})
  }
  resetPassword(passwordResetToken:any, newPassword:any){
    return this.http.post(`${environment.baseurl}/api/auth/savepassword/${passwordResetToken}?newPassword=${newPassword}`,{})
  }
  
}
