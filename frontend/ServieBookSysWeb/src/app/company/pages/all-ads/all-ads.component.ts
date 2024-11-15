import { Component } from '@angular/core';
import { CompanyService } from '../../services/company.service';
import { NzNotificationService } from 'ng-zorro-antd/notification';

@Component({
  selector: 'app-all-ads',
  templateUrl: './all-ads.component.html',
  styleUrls: ['./all-ads.component.scss']
})
export class AllAdsComponent {

  ads:any;

  constructor(private companyService: CompanyService,
      private notification: NzNotificationService
    ){}

  ngOnInit(){
    this.getAllAdsByUserId();
  }

  getAllAdsByUserId(){
    this.companyService.getAllAdsByUserId().subscribe(res=>{
      this.ads = res;
    })
  }

  // 將圖片 URL 格式化為可供使用者檢視的格式。
  updateImg(img){ 
    return 'data:image/jpeg;base64,' + img;
  }

  deleteAd(adId:any){
    this.companyService.deleteAd(adId).subscribe(res=>{
      this.notification
      .success(
        'SUCCESS',
        `Ad Deleted Successfully`,
        {nzDuration: 5000}
      );
      // refresh the page
      this.getAllAdsByUserId();
    })
  }
}
