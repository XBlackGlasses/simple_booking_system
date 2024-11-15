import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { CompanyService } from '../../services/company.service';

@Component({
  selector: 'app-create-ad',
  templateUrl: './create-ad.component.html',
  styleUrls: ['./create-ad.component.scss']
})
export class CreateAdComponent {

  selectedFile: File | null;  // store the image of the ad
  imagePreview: String | ArrayBuffer | null;  // store the preview of the image which will show to the user.(圖片預覽)
  validateForm!: FormGroup;   

  constructor(private fb: FormBuilder,
    private notification: NzNotificationService,
    private router: Router,
    private companyService: CompanyService){}

  ngOnInit(){   // controls for reactive form.
    this.validateForm = this.fb.group({
      serviceName: [null, [Validators.required]],
      description: [null, [Validators.required]],
      price: [null, [Validators.required]]
    })
  }

  onFileSelected(event: any){   // 處理user選擇圖片檔案的事件，更新selectedFile變數，呼叫previewImage()產生圖片預覽
    this.selectedFile = event.target.files[0];
    this.previewImage();
  }

  previewImage(){
    const reader = new FileReader();
    reader.onload = () => {
      this.imagePreview = reader.result;
    }
    reader.readAsDataURL(this.selectedFile);
  }

  postAd(){   // call api to store the ad
    const formData: FormData = new FormData();  // request body
    formData.append('img', this.selectedFile);
    formData.append('serviceName', this.validateForm.get('serviceName').value);
    formData.append('description', this.validateForm.get('description').value);
    formData.append('price', this.validateForm.get('price').value);

    this.companyService.postAd(formData).subscribe(res =>{
      this.notification
        .success(
          'SUCCESS',
          `Ad Posted Successfully`,
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
}
