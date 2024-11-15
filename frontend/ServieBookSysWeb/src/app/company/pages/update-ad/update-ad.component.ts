import { Component } from '@angular/core';
import { CompanyService } from '../../services/company.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { NzNotificationService } from 'ng-zorro-antd/notification';


@Component({
  selector: 'app-update-ad',
  templateUrl: './update-ad.component.html',
  styleUrls: ['./update-ad.component.scss']
})
export class UpdateAdComponent {

  adId:any = this.activatedroute.snapshot.params['id'];   // ActivatedRoute，路由參數中取得ad ID。
  

  selectedFile: File | null;  // store the image of the ad
  imagePreview: String | ArrayBuffer | null;  // store the preview of the image which will show to the user.(圖片預覽)
  validateForm!: FormGroup;   
  
  existingImage: string | null = null;

  imgChanged = false;   // before call api, check the flag. only if true the sent img with api call. 

  constructor(private fb: FormBuilder,
    private notification: NzNotificationService,
    private router: Router,
    private companyService: CompanyService,
    private activatedroute: ActivatedRoute
  ){}
  
  ngOnInit(){   // controls for reactive form.
    console.log('UpdateAdComponent initialized with adId:', this.adId);
    
    this.validateForm = this.fb.group({
      serviceName: [null, [Validators.required]],
      description: [null, [Validators.required]],
      price: [null, [Validators.required]]
    })
    this.getAdById();
  }
  
  onFileSelected(event: any){   // 處理user選擇圖片檔案的事件，更新selectedFile變數，呼叫previewImage()產生圖片預覽
    this.selectedFile = event.target.files[0];
    this.previewImage();
    this.existingImage = null;
    this.imgChanged = true;
  }

  previewImage(){
    const reader = new FileReader();
    reader.onload = () => {
      this.imagePreview = reader.result;
    }
    reader.readAsDataURL(this.selectedFile);
  }
  
  updateAd(){   // call api to store the ad
    const formData: FormData = new FormData();  // request body

    if(this.imgChanged && this.selectedFile){
      formData.append('img', this.selectedFile);

    }

    formData.append('serviceName', this.validateForm.get('serviceName').value);
    formData.append('description', this.validateForm.get('description').value);
    formData.append('price', this.validateForm.get('price').value);

    this.companyService.updateAd(this.adId ,formData).subscribe(res =>{
      this.notification
        .success(
          'SUCCESS',
          `Ad updated Successfully`,
          {nzDuration: 5000}
        );
        // redirect user to view all add
        this.router.navigateByUrl('/company/ads');
    }, error =>{
      this.notification
        .error(
          'ERROR',
          `${error.error}`,
          {nzDuration: 5000}
        );
    });
  }



  getAdById(){
    this.companyService.getAdById(this.adId).subscribe( res =>{
      console.log(res);
      this.validateForm.patchValue(res);
      this.existingImage = 'data:image/jpeg;base64,' + res.returnedImg;
    })
  }

}
