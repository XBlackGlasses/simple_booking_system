import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth/auth.service';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { Router } from '@angular/router';

@Component({
  selector: 'app-signup-client',
  templateUrl: './signup-client.component.html',
  styleUrls: ['./signup-client.component.scss']
})
export class SignupClientComponent {

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
      lastname : [null, [Validators.required]],
      phone : [null],
      password : [null, [Validators.required]],
      checkPassword : [null, [Validators.required]],
    })
  }

  // 提交表格
  submitForm(){
    // 註冊
    this.authService.registerClient(this.validationForm.value).subscribe(res => {
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
