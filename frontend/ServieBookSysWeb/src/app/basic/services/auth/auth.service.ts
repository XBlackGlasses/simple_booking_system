import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { UserStorageService } from '../storage/user-storage.service';
import { environment } from 'src/app/environment/environment';

//const BASIC_URL = "http://localhost:8080/";
//const BASIC_URL = "http://192.168.0.103:8080/";
const BASIC_URL = environment.BASIC_URL;

export const AUTH_HEADER = "authorization";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  // use the http client to call the api in spring boot.
  constructor(private http: HttpClient, private userStorageService: UserStorageService) { }

  //sign up method for client
  registerClient(signupRequestDTO:any):Observable<any>{
    return this.http.post(BASIC_URL + "client/sign-up", signupRequestDTO);   // "client/sign-up" is set in spring boot controller.
  }
  //sign up method for company
  registerCompany(signupRequestDTO:any):Observable<any>{
    return this.http.post(BASIC_URL + "company/sign-up", signupRequestDTO);   // "company/sign-up" is set in spring boot controller.
  }

  // login method
  login (username:string, password:string){
    return this.http.post(BASIC_URL + "authenticate", {username, password}, {observe: 'response'})
    .pipe( // to perform some operations
        map((res: HttpResponse<any>) =>{

          console.log(res.body) // with the body we can get the userId and the Role
          //store the userId and role in our storage, when the user login
          this.userStorageService.saveUser(res.body);
          //calculate the token length with the Authorization
          const tokenLength = res.headers.get(AUTH_HEADER)?.length;
          const bearerToken = res.headers.get(AUTH_HEADER)?.substring(7, tokenLength);// avoid the "bearer " at the beginning
          console.log(bearerToken);
          //store the token in our storage, when the user login
          this.userStorageService.saveToken(bearerToken);
          return res;

        })); 
  }
}
