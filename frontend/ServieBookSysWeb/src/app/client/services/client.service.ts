import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserStorageService } from 'src/app/basic/services/storage/user-storage.service';
import { environment } from 'src/app/environment/environment';

//const BASIC_URL = 'http://localhost:8080/';
//const BASIC_URL = 'http://localhost:8080/api/v1/';
//const BASIC_URL = 'http://192.168.0.103:8080/api/v1/';

const BASIC_URL = environment.BASIC_URL + 'api/v1/';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  constructor(private http: HttpClient) { }

  // get all ads 
  getAllAds(): Observable<any>{
 
    // return this.http.get(BASIC_URL + `api/client/ads`, {
    //   headers: this.createAuthorizationHeader() 
    // });
    return this.http.get(BASIC_URL + `client/ads`, {
      headers: this.createAuthorizationHeader() 
    });
  }

  // search ads by keywords
  searchAdByName(name: any):Observable<any>{
    // return this.http.get(BASIC_URL + `api/client/search/${name}`, {
    //   headers: this.createAuthorizationHeader() 
    // });
    return this.http.get(BASIC_URL + `client/search/${name}`, {
      headers: this.createAuthorizationHeader() 
    });
  }

  // to view the ad details
  getAdDetailsByAdId(adId: any):Observable<any>{
    // return this.http.get(BASIC_URL + `api/client/ad/${adId}`, {
    //   headers: this.createAuthorizationHeader() 
    // });
    return this.http.get(BASIC_URL + `client/ad/${adId}`, {
      headers: this.createAuthorizationHeader() 
    });
  }

  // book a service of ad
  bookService(bookDTO: any):Observable<any>{
    
    // return this.http.post(BASIC_URL + `api/client/book-service`, bookDTO , {
    //   headers: this.createAuthorizationHeader() 
    // });
    return this.http.post(BASIC_URL + `client/book-service`, bookDTO , {
      headers: this.createAuthorizationHeader() 
    });
  }


  getMyBookings():Observable<any>{
    const userId = UserStorageService.getUserId();

    // return this.http.get(BASIC_URL + `api/client/my-bookings/${userId}`, {
    //   headers: this.createAuthorizationHeader() 
    // });
    return this.http.get(BASIC_URL + `client/my-bookings/${userId}`, {
      headers: this.createAuthorizationHeader() 
    });
  }


  giveReview(reviewDTO: any):Observable<any>{
    // return this.http.post(BASIC_URL + `api/client/review`, reviewDTO, {
    //   headers: this.createAuthorizationHeader() 
    // });
    return this.http.post(BASIC_URL + `client/review`, reviewDTO, {
      headers: this.createAuthorizationHeader() 
    });
  }

  // set header and Jwt token.
  createAuthorizationHeader(): HttpHeaders{
    let authHeaders: HttpHeaders = new HttpHeaders();
    return authHeaders.set(
      'Authorization',
      'Bearer ' + UserStorageService.getToken()
    )
  }
}
