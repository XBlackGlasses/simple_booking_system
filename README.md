# Environment
spring boot3
Mysql8.0.4
Angular16
NG-Zorro(Angular UI component library)
java 21

# Description
嘗試前後端分離專案，設計預約服務系統
功能針對client與company兩種user系分為下列幾種
### Company Features:
- Create Account: Register as a company user
- Login: Access the account with credentials
- Post Ads: Advertise services or products
- Update & Delete Ads: Modify or remove posted ads
- View Bookings: See the list of bookings made by clients
- Approve Or Reject Booking: Accept or decline booking requests

### Client Features:
- Create Account: Register as a client user
- Login: Access the account with credentials
- Search Services: Find services or products
- View Ad Details and Reviews: Check detailed information and reviews of ads
- Book Services: Make bookings for services or products
- View Bookings: See the list of bookings made by the client
- Review Services: Provide feedback or reviews on services

# 網頁預覽
### 註冊 / 登入
<img src=".\regist.png" alt="regist" style="zoom:75%;" />  <img src=".\login.png" alt="login" style="zoom:75%;" />

### client： 遊覽服務、預約狀態、預約及評論
<img src=".\user_dash.png" alt="user_dash" style="zoom:75%;" /> 
<img src=".\book_status.png" alt="book_status" style="zoom:75%;" />
<img src=".\view.png" alt="view" style="zoom:75%;" />

### company：服務確認、刊登服務廣告、服務列表
<img src=".\com_dash.png" alt="com_dash" style="zoom:75%;" />
<img src=".\post_ad.png" alt="post_ad" style="zoom:75%;" />
<img src=".\ads.png" alt="ads" style="zoom:75%;" />

# spring boot dependencies & application.properties

```
Spring Wed
Spring Data JPA
Lombok
MySQL Driver
jwt
```

```properties
spring.application.name=booking
# service_booking_system_db is name of dataset
spring.datasource.url=jdbc:mysql://{your_ip}:3306/service_booking_system_db?serverTimezone=Asia/Taipei&characterEncoding=utf-8
spring.datasource.username=root
spring.datasource.password=******
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# avoid missing data when reload hibernate
spring.jpa.hibernate.ddl-auto=update
# show sql statement
spring.jpa.show-sql=true
```

細部內容參考setting.md
