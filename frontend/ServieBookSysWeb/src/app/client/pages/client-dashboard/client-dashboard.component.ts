import { Component } from '@angular/core';
import { ClientService } from '../../services/client.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-client-dashboard',
  templateUrl: './client-dashboard.component.html',
  styleUrls: ['./client-dashboard.component.scss']
})
export class ClientDashboardComponent {

  ads: any = [];
  validateForm !: FormGroup;

  constructor(private clientService: ClientService,
    private fb: FormBuilder
  ){}
  
  
  ngOnInit(){
    this.validateForm = this.fb.group({
      service: [null, [Validators.required]]
    })
    this.getAllAds();
  }

  searchAdByName(){
    this.clientService.searchAdByName(this.validateForm.get(['service']).value).subscribe(res=>{
      this.ads = res;
      
    })
  }
  
  
  getAllAds(){
    this.clientService.getAllAds().subscribe(res=>{
      this.ads = res;
    })
  }


  // 將圖片 URL 格式化為可供使用者檢視的格式。
  updateImg(img){ 
    return 'data:image/jpeg;base64,' + img;
  }
}
