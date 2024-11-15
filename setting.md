## Enviroment

spring boot3

Mysql8.0.4 + workbench

Angular16

NG-Zorro(Angular UI component library)

java 21

## Description

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



## Setting : Spring Boot

#### Dependencies

```
Spring Wed
Spring Data JPA
Lombok
MySQL Driver
jwt
```



#### Application.properties

```properties
spring.application.name=booking
# service_booking_system_db is name of dataset
spring.datasource.url=jdbc:mysql://{your_id}:3306/service_booking_system_db?serverTimezone=Asia/Taipei&characterEncoding=utf-8
spring.datasource.username=root
spring.datasource.password=******
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# avoid missing data when reload hibernate
spring.jpa.hibernate.ddl-auto=update
# show sql statement
spring.jpa.show-sql=true	
```



## Setting Angular

#### create Angular Project (with routing & scss)

```
ng new {projectNmae}
```



#### create a feature module with routing

VSCode開啟專案後在終端機輸入下兩行 ，{company}、{client}是自行設定的名稱，是一個功能模組並帶有要路由到的component。

```
ng generate module {company} --route {company} --module app.module

ng generate module {client} --route {client} --module app.module
```



#### basic component

在app dir.下新建basic dir.，裡面放置需要的components和services，新建這兩個dirs。

在components dir.中透過下面指定建立相關component，會分別產生各自的資料夾與程式。我們希望程式有login、signup、signupClient(用戶註冊表)、signupCompany(公司註冊表)等元件。

-g : generate.  -c : component

```powershell
# -- the terminal path .../src/app/basic/components
# generate login component
ng g c login

# generate signup component
ng g c signup

# generate signupClient component
ng g c signupClient

# generate signupCompany component
ng g c signupCompany
```



services dir.中建立auth、storage裡個資料夾

##### in auth and storage dir. open the terminal and type below command, perspectively

s : service

```powershell
# in auth dir
ng g s auth

# in storage dir
ng g s storage
```



在**app-routing.module.ts**中的routes加入下面signupClientComponent、signupCompanyComponent物件。這些元件都是會需要更改router獲取頁面。

```typescript
const routes: Routes = [
  {path:  'register_client', component: SignupClientComponent},
  { path: 'company', loadChildren: () => import('./company/company.module').then(m => m.CompanyModule) }, 
  { path: 'client', loadChildren: () => import('./client/client.module').then(m => m.ClientModule) }
];
```



#### Install ng-zorro

in VSCode terminal(project dir.)

```
ng add ng-zorro-antd
```

Questions：

* enable icon dynamic .... : yes
* set up custom theme .... : no
* locale code : en_US
* template to create project : blank

​	

add the DemoNgZorroAntdModule.ts(import component from ng-zorro) file to the project.

in **app.module.ts** add imports like belows( add DemoNgZorroAntdModule & activeFormsModule) !!(client、service的功能模組下的 client.module.ts / company.module.ts 也都要加入)

```typescript
imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    DemoNgZorroAntdModule,
    ReactiveFormsModule
  ],
```



在 client、company這兩個routing module中分別新增pages、services資料夾，在兩個pages資料夾下建立 dashboard component

```
# in client/pages
ng g c clientDashboard

# in company/pages
ng g c companyDashboard
```

修改**app.component.scss**



## spring boot

create entity package to save user class

User class 做為承接 user database映射到spring boot 的物件

```java
// User class in entity package
// the User dataset

// 告訴Spring這是數據模型層的宣告
@Entity
// 標註這個object映射的資料庫格式，指定為"user" Table
@Table(name="user")
class{
    id,
    email,
    password,
    name,
    lastName,
    phone,
    role	// client, company
}
```



create repository package.

這個package負責處理與資料庫的銜接，UerRepository實做JpaRepository interface，提供資料庫基本操作，讓class實作後可以操作資料庫。



**Dao**：`Data Access Object` 資料存取物件，資料儲存的相關操作從原本架構解耦，即降低程式與資料庫的相依性，獨立出一個專門處理相關事務邏輯的物件，達到不同資料庫的統一存取方法。

**Dto**： `Data Transfer Object` 資料傳輸物件，傳輸資料所使用，使用 DTO 可以讓我們減少參數傳遞的混亂，增加程式可讀性。可以讓我們將一些必要傳遞但不希望被操作的資料進行封裝，或如果業務需要增加傳遞資料或對傳遞資料進行特定處理，只需要在物件增加欄位或修改即可。(簡單理解，Dao會映射整體database資料格式，但程式運作實不一定會需要全部的資訊，Dto會將不需要資訊剃除，只處理需要的資料)

>[參考網站](https://www.youtube.com/watch?v=Bl39p7SfuJk)
>
>> Controller(presentation) - Service(Application) - Dao(Infrastructure)：從前端從左到右的順序取得DB的資料，這種架構彼此間的依賴性是較高的，比如service對Dao的依賴性較高，Dao則對database或第三方library的依賴性較高。這個架構的service容易龐大，對於大project的維護相對困難；此外對Dao的依賴性太高，當今天想升級database或第三方library，需要對project進行大量改動；當project變大，想進行micro-service架構難度會增加。
>
>
>
>> [DDD補充](https://ithelp.ithome.com.tw/articles/10222162) 基本上都會結合clean architecture
>>
>> 在domain driven design(DDD)設計架構中，Application到 Infrastructure中新增Domain layer，業務邏輯一般可以分為兩大類：一個是固定不變的，比如下單的，它的核心內容可能不變，會放在Domain這層；Application主要負責接收不同的use case(應用場合)，也就是基於不同場合會產生不同的業務邏輯，比如client登入或company登入，它的業務邏輯可能不同，這些邏輯就會放在service layer。也就是說service這層主要存放會對domain產生不同影響的業務邏輯，domain用來放相對穩定或完全不變的邏輯。
>> 以往Infrastructure這層大部分包含DB或第三方library，比如message queue、catch，因此其他layers盡可能與這層解耦。如下圖將原本的架構修改為Infra平行於其他三層，Infra依賴於已有的三層，其他三層沒有任何對Infra的依賴。
>> presentation與application間的數據傳輸為Rest DTO物件，Rest DTO定義的是後端與用戶間的傳輸數據類型，不能隨意改變。
>> application與domain間傳輸的資料稱為 Entity，domain layer的業務邏輯可能發生變化，entity可以承載更多狀態。為了讓layers間的數據結構解耦，所以在pres.->app.、 app.->domain間的用不同數據的定義。在application layer取得DTO資料後，就要將data轉為entity型態。
>> domain 與 infra. layer的傳輸數據定義為database DTO，這層就是常定義的database schema，和Rest DTO一樣不能隨意修改。因此Domain layer在處理數據時需要對Entity和database DTO轉換。
>>
>> <img src="C:\Users\joseg\AppData\Roaming\Typora\typora-user-images\image-20241030001809187.png" alt="image-20241030001809187" style="zoom: 80%;" />
>>
>> 對應各層在實作上通常會設計特定module，如下圖的Web例子：
>>
>> Web(presentation layer)：一般放controller，後端對應到前端應用。
>>
>> web application(application)：包含各種service，如同第一段所說存放會對domain產生不同影響的service。通常定義DTO的package也是放在這層，RestDTO可能被放在{app}.share結尾的package。
>>
>> Domain：根據主要service不同命名不同domain，比如訂單(Order)、支付(payment)。這層包含domain service(用manager取名可以跟application service區分)、Entity以及data repository。repository是對於Dto更上層的封裝，可以讓domain訪問database，如果是複雜的專案，有時候也需要定義factory，對repository進一步封裝。domain service可能會以{}manager命名，一般會被共享給application layer，用於使用domain的某個業務邏輯，web application要創建訂單，通常是把domain manager的物件依賴注入到application layer，再使用manager的相應方法對訂單操作。domain layer也會封裝一些共用內容(domain share)，比如定義database DTO、DAO interface(訪問database的interface)，定義後的DAO interface由infrastructure layer實現，repository能透過DAO interface訪問database，且repository不知道DAO實現方法，因此能解耦domain與infrastructure database。 Domain layer中主要的業務邏輯是被entity以及manager管理，repository只作為數據操作的interface。在依賴關係上，entity不應該使用manager或repository，manager是調用不同entity之間或entity與外部操作所需的業務邏輯。一般情況是application service調用manager去取得entity。
>>
>> infrastructure(假設使用ORM)：依賴domain share，實現其中的interface，以及資料庫操作。
>>
>> <img src="C:\Users\joseg\AppData\Roaming\Typora\typora-user-images\image-20241030143242056.png" alt="image-20241030143242056" style="zoom: 67%;" />
>>
>> 



create services package.

放置業務邏輯相關的class，包含AuthService：登入驗證相關。

(前端開始處理用戶註冊表的設計)



建立configs package，建立SimpleCorsFilter class，這個class允許Angular 前端能夠使用spring boot app.的 api。

Filter能讓我們在Request進入Controller之前或Response後且回覆客戶端以前執行一些業務邏輯。底層是透過一連串的filter完成

```
CORS全名叫 Cross-Origin Resource Sharing ，一種用來解決同源政策(Same-origin policy)。
	Same-origin policy：遊覽器禁止從A網站使用AJAX(XMLHttpRequest or Fetch) 呼叫 B 網站的 API。
	同源指的是網域名稱、協定、通訊埠(port)都相同。
	ex. http://www.hahaha.com/index.html 呼叫 http://www.hahaha.com/server.jsp : 不是跨網域
		http://www.hahaha.com:8080/index.html 呼叫 http://www.hahaha.com/server.jsp : 跨網域，port不同
        http://www.hahaha.com/index.html 呼叫 http://www.ha.com/server.jsp : 跨網域，主網域不同
CORS能讓前後端跨網域存取資源，基本設定都在後端
```

[CORS與同源介紹](https://medium.com/starbugs/%E5%BC%84%E6%87%82%E5%90%8C%E6%BA%90%E6%94%BF%E7%AD%96-same-origin-policy-%E8%88%87%E8%B7%A8%E7%B6%B2%E5%9F%9F-cors-e2e5c1a53a19)





#### 驗證(authentication與授權(authorization)  - JWT 

##### 常見流程：

前端傳送客戶資訊(ex : username, password)，後端對客戶訊息到資料庫中進行認證，認證通過後透過客戶訊息產生Token(權杖)，將Token響應給前端客戶，前端保存這個Token。之後客戶在請求其他功能時都要攜帶這個Token，後端認證沒問題才能讓前端客戶使用，相當於授權給客戶使用功能。 server不用保存Token，從Token decoder就能得到客戶相關訊息，因此存處負擔不大。



原本只用session保存客戶資訊，會對每個登入server的client發送session id存在用戶的cookies，server也保存session id，之後client發請求時server透過對應session id來得知那些client是登入的。

問題：

* 在近期流行前後端分離的系統中，每個用戶在驗證後都會在server端記錄一次，方便下次請求的辨識。通常session是存在記憶體中，隨著用戶量增加，server的負擔也會變重。
* 用戶認證後，server保存驗證紀錄，如果是保存在記憶體中，表示用戶下次的請求必須要在一樣的server上才能取得授權的資源，在分布式的應用上，限制附載均衡的能力。
* 因為是基於cookies的驗證，cookies被攔截用戶可能會受到跨站請求偽造的攻擊(CSRF)。



​	在utill package建立JwtUtil class 去定義產生與解析JWT的方法。

​	在services package 中新增jwt package，建立UserDetailsServiceImpl class裡面實作用戶的訪問，byemail。

​	在services/jwt package中新增JwtRequestFilter class，加入授權的filter，讓持有token的請求通過。

​	在config package中新建WebSecurityConfig class，讓我們對網站做權限管理，

​	在dto package 新建AuthenticationRequest class，作為承接來自"/authenticate"API(for login)的class。

​	

​	在AuthenticationController.class 增加"/authenticate" API，讓authenticate(login)的client得到token。



#### 建立服務項目資訊的data and service

##### (For Company相關)：新增廣告、列出廣告、修改及刪除

建立 Ad entity、 AdDTO、 AdRepository、 CompanyService(contact with userRepository、AdRepository)、CompanyController

(新增)

在 **companyServiceImpl** 中建立了一個名為 **postAdd** 的方法，這個方法接收使用者 ID 和廣告的 DTO 物件作為參數。如果user存在則創造廣告物件，

CompanyController，創造前端呼叫的api (@PostMapping("/ad/{userId}") )，讓前端建立服務廣告。

(顯示)

**companyServiceImpl** 中建立 getAllAds method，找到這個userId的所有服務。(這間公司的所有服務)

CompanyController 創建前端呼叫的 API (@GetMapping("/ads/{userId}") )，使用service中的getAllAds method。

(修改)

**companyServiceImpl** 中建立 getAdById、updateAd method，

CompanyController 創建前端呼叫的 API (@GetMapping("/ad/{adId}") )，使用service中的getAllAds method。

CompanyController 創建前端呼叫的 API (@PutMapping("/ad/{adId}") )，修改已經刊登的廣告。

(刪除)

**companyServiceImpl** 中建立 deleteAd method

CompanyController 創建前端呼叫的 API (@DeleteMapping("/ad/{adId}") )

##### (For client 相關)

建立client service package in service package，裡面放ClinetService的interface & implement。

controller package中新增 ClientController。

(顯示)

**ClinetServiceImpl** 中新增 getAllAds method。

ClientController 創建前端呼叫的 API (@GetMapping("/ads") )

(顯示:搜尋)

adRepository 新增 findAllByServiceNameContaining method， containing是Jpa 的keyword。

**ClinetServiceImpl** 中新增 searchAdByName method。

ClientController 創建前端呼叫的 API (@GetMapping("/search/{name}") )



#### 新增預約表單database(預約時需要包含user、company、ad的資訊) Reservation.java

> !!!注意 程式中的ReservationState enum 拼錯了，應該是ReservationStatus。前後端用到這個變數的地方都要注意

entity package中新增Reservation class，裡面有兩個enum分別是預約狀態、審核狀態。

repository package新增ReservationRepository。

新增ReservationDto，作為與前端互動的資料型態。

**ClinetServiceImpl** 中新增 bookService method，client要預約服務時，確認前端傳來的ad以及user存在，就會建立預約表單，儲存user、company、ad以及預約狀態等資訊。

ClientController創建前端呼叫API (@PostMapping("/book-service") )





#### 新增review database(關於這個ad的評論，包含user, ad data) Review.java

repository package新增ReviewRepository。

新增ReviewDTO，作為與前端互動的資料型態。

新稱AdDetailsForClientDTO，保存關於這個ad的所以評論(List of ReviewDTO)



**ClinetServiceImpl** 建立AdDetailsByAdId method，取得關於這個ad的所以評論。

ClientController創建前端呼叫API (@GetMapping("/ad/{adId}") ) 



ReservationRepository新增 findAllByCompanyId method，取得資料庫中跟這個company相關的所有廣告。

**CompanyServiceImpl**  新增 getAllbookings method， 使用上列資料庫取得的data。

CompanyController創建前端呼叫API (@GetMapping("/bookings/{companyId}") ) 



**CompanyServiceImpl**  新增 changeBookingStatus method，修改預約資料的預約狀態。

CompanyController創建前端呼叫API (@GetMapping("/booking/{bookingId}/{status}") ) 



**ClinetServiceImpl** 建立getAllbooingsByUserId method，取得所有預約的結果。

ClientController創建前端呼叫API (@GetMapping("my-bookings/{userId}") ) 



**ClinetServiceImpl** 建立giveReview method，新增一則評論data。

ClientController創建前端呼叫API (@PostMapping("/review") ) 







## Angular

##### (用戶註冊表相關設計)

/app/basic/components/signup-client中，建立客戶註冊所需元件，包含signup表單，以及相關的http畫面。

在signupCompany元件中，與上面流程相同建立跟公司註冊元件，包含表單及跳轉的http畫面。

app/basic/sevices，負責處理sevices相關component.。在auth/auth_service.ts 新增負責前端呼叫後端auth相關controller api 的methods。用戶註冊時透過這裡呼叫後端的API。

在app-routing.module.ts中加入register_company/register_client路徑以及相應的components.



透過spring boot中config package的SimpleCorsFilter class，可讓前端能夠使用spring boot app.的 api。透過CORS技術讓前後端串聯。



##### (後端處理完JWT用戶認證授權後設計前端login page，Angular呼叫login相關的API)

在app/basic/sevicesauth/auth_service.ts 加入login service，處理前端呼叫後端API的銜接。

在app/basic/components/login中的login.component.ts 建立login表單的設計。login.component.html、login.component.scss設計網頁畫面。

在auth/auth_service.ts 新增 login method，呼叫後端athenticate API，並取得jwt token。

在app-routing.module.ts中加入login 路徑以及相應的components.



##### (用戶儲存，保存用戶details以及獲取)

這邊的資料是存在client-side storage，也就是遊覽器本身，使用時不需向server請求。(server-side storage通常是存在像MYSQL這類的database)。 [補充資料](https://jscrambler.com/blog/working-with-angular-local-storage)

在user-storage.service.ts加入判斷客戶or公司是否登入的功能，這邊會用到client-side storage的操作：(tsconfig.json中的"strict"選項能改false，減少類型要求)

在auth-service中加入user-storageservice的功能。

> 在前端開啟的網頁中，實際登入後能在網頁"檢視"中，找到application，其中的storage/local storage欄位能找到保存的資料



##### (登入後根據Role跳轉頁面)

app-routing.module.ts 加入login route 和 signupComponent。

在app.component.ts 中 加入判斷客戶或公司是否登入的邏輯，以及登入後跳轉到 "login" url。

app.component.html設計客戶與公司登入頁面。

login.component.ts 加入router 根據使用者的角色跳轉不同url功能。



##### (client / company dashboard)

在.../app/client/client-routing.module.ts中加入 ClientDashboardComponent 的元件與路徑。

.../app/client/client-routing.module.ts中也執行相同操作。



##### (後端建立廣告服務相關資料、service以及api後) 建立前端api呼叫、JWT權杖處理。

##### (@PostMapping("/ad/{userId}") API 相關元件)

在.../app/company/services 資料夾下開啟terminal，輸入下面指令建立company service

```
ng g s company
```

​	在其中的company.service.ts中設定呼叫後端api的function，並且設定回傳header中要含有token。



###### 建立一個新廣告頁面，並使用反應式表格來處理用戶輸入，最後透過 API 將廣告信息發布到後端：

​	在 .../app/company/pages 透過下面指定建立 create-ad component

```
ng g c createAd
```

將這個component加入 company-routing.module.ts 的 routes中

create-ad.component.ts  設計反應試表格建立程式，以及http按鍵事件，create-ad.component.httl 設計廣告刊登頁面，包含加入圖片、描述服務、服務價錢，以及發送api到後端。



##### company(使用 @GetMapping("/ads/{userId}" API 的相關元件)

.../app/company/services/company.service.ts 中加入 getAllAdsByUserId method。

.../app/company/pages資料夾下開啟termial，用下面指令建立allAds component，用來顯示當前公司的所有廣告

```
ng g c allAds
```

add-ads.component.ts中建立 getAllAdsByUserId method，以及更新圖片的 method。 Html、SCSS製作網頁。

將component加到 company-routing.module.ts 的 routes中。



##### company(使用 @GetMapping("/ad/{adId}")" API 的相關元件) : 完成Ads網頁中的update button以及網頁，更新廣告資料

.../app/company/services/company.service.ts 中加入  getAdById method。

透過如上面指令，新增updateAd component。

將component加到 company-routing.module.ts 的 routes中。

update-ad.component.ts 建立呼叫service getAdById的method。



.../app/company/services/company.service.ts 中加入 updateAd method。

基本上.ts 、.html都是從create-ad component改來的。Html修改部分文字，圖片顯示邏輯。.ts 修改確認圖片是否修改，以及put api call ，getAdById 增加圖片儲存。



##### company(使用 @DeleteMapping("/ad/{adId}") API 的相關元件) : 完成Ads網頁中的delete button以及刪除廣告功能

.../app/company/services/company.service.ts 中加入 deleteAd method

修改add-ads components：.ts file加入 deleteAd method，.html file 修改delete button的 click function。





##### client(@GetMapping("/ads") API 相關元件)：顯示client當前能看到的所有service ads.

建立 client service：在.../app/client/services 中打開terminal輸入下面指令建立servie

```
ng g s client
```

client.service.ts 中設定BASIC URL：接到後端URL，透過HttpClient 使用http傳輸功能，建立getAlllAds method 呼叫後端api。 將company.service.ts 中的 驗證相關method 複製過來，兩者驗證方法相同。

client.module.ts 中imports  DemoNgZorroAntdModule, ReactiveFormsModule, FormsModule。

在 client-dashboard.component.ts中建立 getAllAds method，呼叫service的method，以及轉換圖片格式的method讓html顯示圖片。 設計Html 頁面。



##### client(@GetMapping("/search/{name}")  API 相關元件)：搜尋特定名稱的廣告

client.service.ts 中新增 searchAdByName method，將前端輸入的name 透過api傳到後端搜尋。

client-dashboard.component.ts 新增互動表單的物件，新增searchAdByName method 將網頁互動表單中輸入的serviceName 傳到 client.service 的method並呼叫後端api，將回傳的物件存在ads array並在網頁顯示。

client-dashboard.component.html 新增互動表單



##### client(@GetMapping("/ad/{adId}")  API 相關元件)：取得廣告詳細資料(Ex 評論)

client.service.ts 中新增 getAdDetailsByAdId method，將前端選取的ad id 透過api傳到後端搜尋，接收後端回傳的ad details。

在client pages中新增 adDetails component

```
// terminal opened in client/pages
ng g c adDetail
```

client-routing.module.ts中新增adDetail component route。(前面設計的client dashboard html中，view button按下後就會跳轉到這個component)

在client/pages/ad-detail.component.ts 中建立constructor，並加入getAdDetailsByAdId method，網頁初始時就會呼叫它。設計html and scss。



##### client(@PostMapping("/book-service") API 相關元件)：客戶預約服務，後端建立reservation data

client.service.ts 中新增bookService method， 呼叫後端api，傳送bookDTO(預約相關訊息)，

在client/pages/ad-detail.component.ts 中建立 互動表單，讓使用者選擇預約日期。新增bookService method(html網頁book button響應的method)，建立bookDTO並呼叫client.service.ts中的bookService method。



##### company(@GetMapping("/bookings/{companyId}") API 相關元件)：列出所有預約資料

company.service.ts  新增 getAllBookings method，呼叫後端API，並取得所有預約資料。

在company-dashboard.component.ts 中建立 getAllBookings method，呼叫 company service.ts中的 method， 在html中列出預約資料。



##### company(@GetMapping("/booking/{bookingId}/{status}") API 相關元件)：改變預約狀態

company.service.ts  新增 changeBookingStatus method，傳遞booking id & status給後端。

在company-dashboard.component.ts 中新增changeBookingStatus method， call service method，傳送book id & status，連動到html中的button。



##### client(@GetMapping("my-bookings/{userId}")  API 相關元件)：取得預約資訊

client.service.ts 中新增getMybookings method。

在client pages中新增 MyBookings component

```
// terminal opened in client/pages
ng g c MyBookings 
```

client-routing.module.ts中新增MyBookings  component route。

my-bookings .component.ts 中新增getMybookings methos，呼叫client service中的getMybookings method。在html中將取得的資訊畫在網頁上。



##### client(@PostMapping("/review")   API 相關元件)：創立一則評論

client.service.ts 中新增giveReview method。

在client pages中新增 review component

```
// terminal opened in client/pages
ng g c review
```

client-routing.module.ts中新增review  component route。

review.component.ts 建立互動表單， 新增giveReview method，call service.ts的giveReview並傳遞表單的訊息。html製作網頁。MyBookings html中製作的review button 連動到這個component 的route。





## Unit test : in SpringBoot

[參考網址](https://yen0304.github.io/p/springboot%E5%AD%B8%E7%BF%92%E7%AD%86%E8%A8%98-%E5%96%AE%E5%85%83%E6%B8%AC%E8%A9%A63-controller%E5%B1%A4dao%E5%B1%A4service%E5%B1%A4%E7%9A%84%E6%B8%AC%E8%A9%A6%E6%96%B9%E5%BC%8Fmockmvc%E4%BD%BF%E7%94%A8%E8%A9%B3%E8%A7%A3/)	有些用法要修改(看bookservice project的test code)

[補充範例](https://www.youtube.com/watch?v=4l3EFprMqpU&list=PL82C6-O4XrHcg8sNwpoDDhcxUCbFy855E&index=5) Mockito

#### Junit5

class 前加入 @SpringBootTest ：會在單元測試時啟動Spring容器，也會創建Bean出來，等同運行springboot程式。

測試的function前面加入@Test，function必須是public，return void。

@Transactional：如果測試時對資料庫增減data，實際上真的會保存在資料庫，透過@Transactional，在單元測試結束之後，SpringBoot會去rollback（回滾）這個單元測試裡面所有的資料庫操作。

mysql中的transaction功能在於讓一個連續性的DB操作要嘛全部成功，不然就全失敗

#### Mockito：

模擬Http呼叫，在local端完成methods呼叫。

除了在測試用的class上面加上@SpringBootTest之外，要另外加上註解@AutoConfigureMockMvc這個註解，再注入MockMvc這項Bean。

RequestBuilder，他會決定要發起的http requst，url路徑甚至header，他其實就是一個APItester的概念。

mockMvc.perform()程式，他的用途就是在執行上面的requestBuilder，這個方法就等同於在APItester按下send。



(CompanyControllerTest 案例)

透過在class上的@AutoConfigureMockMvc(addFilters = false)  關閉filter

如果模擬的api有參數(ex. "/api/company/ad/{userId}")，在後面加上想要的變數就好(ex. post("/api/company/ad/{userId}", 1))，會自動補上。

前端傳來的屬性用@ModelAttribute 接的話，測試設定Mock builder時用 flashAttr()設定模擬值。



mockito也能創造一個假的bean，替換掉容器原有的bean。比如要模擬A class的方法，直接指定呼叫該方法時的回傳結果，要注意如果要模擬A class的方法，A class需要用@MockBean這個註解標是為模擬用的bean。

主要能夠：

- 模擬方法的返回值
- 模擬拋出Exception
- 記錄方法的使用次數、順序

限制

- 不能 mock 靜態方法
- 不能 mock private 方法
- 不能 mock final class



可以depence JsonPath libray方便解析傳遞資料。

[Restfull api測試](https://vivifish.medium.com/spring-boot-%E4%BD%BF%E7%94%A8-mockmvc-%E6%B8%AC%E8%A9%A6-controller-b9486c2ac1e6)

## 坑

spring boot dependencies 不要引入<artifactID> jjwt， 有jjwt-api、jjwt-impl、jjwt-jackson就好，不然報錯(java.lang.NoSuchMethodError: io.jsonwebtoken.SignatureAlgorithm.getMinKeyLength() )

Secrete Key 長度不能太短，不然也是報錯

驗證時前後端的參數名稱要一樣，比如AuthenticationController 的 createAuthenticationToken，傳入的是AuthenticationRequest class，裡面有"username"、"password"兩個屬性，前端傳入時也必須用"username"、"password"作為json key 傳入，大小寫都要一樣不然會報錯。(TMD這種bug spring boot code看到天荒到老都找不到)



unit test時候，如果測試的api會經會授權相關的filter，透過在class上的@AutoConfigureMockMvc(addFilters = false)  關閉filter