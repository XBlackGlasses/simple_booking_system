import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { AuthService } from '../../services/auth/auth.service';

@Component({
  selector: 'app-signup-company',
  templateUrl: './signup-company.component.html',
  styleUrls: ['./signup-company.component.scss']
})
export class SignupCompanyComponent {
  
  // almost same as SignupClientComponent. => last name is changed to address
  validationForm!: FormGroup;   // ! = 要求變數(validationForm)一定要存在
  constructor(private fb: FormBuilder,  // 建立表單
    private authService: AuthService,   // defined in ../app/basic/services/auth.service.ts
    private notification: NzNotificationService,
    private router: Router){}
  
  // 初始化葉面內容，顯示數據綁定、設置 directive 和輸入屬性
  ngOnInit(){
    // 驗證表單
    this.validationForm = this.fb.group({
      email: [null, [Validators.email, Validators.required]],
      name : [null, [Validators.required]],
      address : [null, [Validators.required]],
      phone : [null],
      password : [null, [Validators.required]],
      checkPassword : [null, [Validators.required]],
    })
  }

  // 提交表格
  submitForm(){
    // 註冊
    this.authService.registerCompany(this.validationForm.value).subscribe(res => {
      // 成功=>通知成功
      this.notification
      .success(
        'SUCCESS',
        `Signup successful`,
        {nzDuration: 5000}
      );
      // 畫面導向login
      this.router.navigateByUrl('/login');
    }, error =>{
      // 失敗=>通知失敗
      this.notification
      .error(
        'ERROR',
        `${error.error}`,
        {nzDuration: 5000}
      );
    })
  }
}
