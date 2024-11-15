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
export class CompanyService {

  constructor(private http: HttpClient) { }
  
  // create new ad.
  postAd(adDTO: any): Observable<any>{
    const userId = UserStorageService.getUserId();
    
    // return this.http.post(BASIC_URL + `api/company/ad/${userId}`, adDTO, {
    //   headers: this.createAuthorizationHeader() 
    // });
    return this.http.post(BASIC_URL + `company/ad/${userId}`, adDTO, {
      headers: this.createAuthorizationHeader() 
    });
  }

  // get all ads about this company
  getAllAdsByUserId(): Observable<any>{
    const userId = UserStorageService.getUserId();
    
    // return this.http.get(BASIC_URL + `api/company/ads/${userId}`, {
    //   headers: this.createAuthorizationHeader() 
    // });
    return this.http.get(BASIC_URL + `company/ads/${userId}`, {
      headers: this.createAuthorizationHeader() 
    });
  }

  // get ad by ad-index
  getAdById(adId:any): Observable<any>{
      
    // return this.http.get(BASIC_URL + `api/company/ad/${adId}`, {
    //   headers: this.createAuthorizationHeader() 
    // });
    return this.http.get(BASIC_URL + `company/ad/${adId}`, {
      headers: this.createAuthorizationHeader() 
    });
  }

  // update ad data.
  updateAd(adId: any, adDTO: any): Observable<any>{
    // return this.http.put(BASIC_URL + `api/company/ad/${adId}`, adDTO, {
    //   headers: this.createAuthorizationHeader() 
    // });
    return this.http.put(BASIC_URL + `company/ad/${adId}`, adDTO, {
      headers: this.createAuthorizationHeader() 
    });
  }

  // delete ad
  deleteAd(adId: any): Observable<any>{
    // return this.http.delete(BASIC_URL + `api/company/ad/${adId}`, {
    //   headers: this.createAuthorizationHeader() 
    // });
    return this.http.delete(BASIC_URL + `company/ad/${adId}`, {
      headers: this.createAuthorizationHeader() 
    });
  }

  // list all ads that be booked
  getAllAdBookings(): Observable<any>{
    const companyId = UserStorageService.getUserId();
    // return this.http.get(BASIC_URL + `api/company/bookings/${companyId}`, {
    //   headers: this.createAuthorizationHeader() 
    // });
    return this.http.get(BASIC_URL + `company/bookings/${companyId}`, {
      headers: this.createAuthorizationHeader() 
    });
  }


  changeBookingStatus(bookingId: number, status: string): Observable<any>{
    
    // return this.http.get(BASIC_URL + `api/company/booking/${bookingId}/${status}`, {
    //   headers: this.createAuthorizationHeader() 
    // });
    return this.http.get(BASIC_URL + `company/booking/${bookingId}/${status}`, {
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
