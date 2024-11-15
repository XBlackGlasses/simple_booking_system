# 部屬

backend ： spring boot + mysql

frontend：Angular + nginx

## Docker install in windows

開啟windows中的 [Hyper-V]、[windows子系統Linux版]就能安裝docker desktop。



## Database

#### 匯出Mysql Database in windows：

使用mysqldump工具會出資料夾，window中比較麻煩要找到mysqldump.exe file，依照每個人安裝路徑不同在不同位置，用下列指定產出當前環境(本機)下的資料庫訊息。

```
"[yourPaht_1]\MySQL Server 8.0\bin\mysqldump.exe" -u root -p my_database >  "[yourPath_2]\backup.sql"
```



#### 透過Docker啟動MySQL：

透過 Docker 抓取 Mysql image，這邊使用mysql version 8

```
docker pull mysql:8
```



透過docker run {image}執行docker image。

-p ：是指定port number，docker下載的MySQL預設的port也是3306，為了避免跟本地(本機)mysql撞號，將docker對外的port號設為3307。 

-e ：是設定環境變數 主要設定 mysql root password、mysql database，因為是用root進行mysql操作，user就沒特別填。

-d：讓docker container在後台運行，並返回容器ID。

--name：命名運行起來的container。

透過下面指令創建docker container並啟動MySQL

```
docker run --name mysql3307 -d -p 3307:3306 -e MYSQL_ROOT_PASSWORD=mysql -e MYSQL_DATABASE=service_booking_system_db -e MYSQL_PASSWORD=mysql mysql:8
```



進入mysql docker bash執行環境，執行mysql 登入指定並輸入密碼就能看到歡迎登入。

```
# 進入docker bash
docker exec -it mysql3307 bash

# 執行MySQL
mysql -u root -p
```



將本機產出的database.sql複製到docker中，透過下列指定匯入sql檔案。

```bash
docker cp <path>/<your_import_sql> <mysql container name>:<container path>/<your_import_sql>
```

套用到這個project，backup.sql放在spring boot專案root下的database dir.， 在spring boot terminal輸入下面指令

```
docker cp ./database\backup.sql mysql3307:var/lib/mysql/service_booking_system_db.sql
```

複製完成會顯示成功

![image-20241111025358140](C:\Users\joseg\AppData\Roaming\Typora\typora-user-images\image-20241111025358140.png)



回到mysql3307 container中，進入mysql開啟目標database 並 用下面指令取得要匯入的檔案

```
source <your_path>/<your_file_name>
```

<img src="C:\Users\joseg\AppData\Roaming\Typora\typora-user-images\image-20241111025905255.png" alt="image-20241111025905255" style="zoom: 67%;" />

這時就能用spring boot連線到這個mysql database了。



#### 停止/啟動 本地mysql服務：

docker port不能跟本地mysql port相同，本地通常是3306，如果docker port也對到3306會無法執行，可以關閉本地mysql服務，或者將docker port對應到本地的其他port  ***ex：docker  run -p 3307:3306 .....***



win+R在搜尋內打 **services.msc**

找到mysql 80，按右鍵停止、啟動或重新啟動的服務選項。



#### Docker 常用參數：

- **`-d`**: 后台运行容器并返回容器 ID。
- **`-it`**: 交互式运行容器，分配一个伪终端。
- **`--name`**: 给容器指定一个名称。
- **`-p`**: 端口映射，格式为 `host_port:container_port`。
- **`-v`**: 挂载卷，格式为 `host_dir:container_dir`。
  - [講解](https://larrylu.blog/using-volumn-to-persist-data-in-container-a3640cc92ce4)
- **`--rm`**: 容器停止后自动删除容器。
- **`--env` 或 `-e`**: 设置环境变量。
- **`--network`**: 指定容器的网络模式。
- **`--restart`**: 容器的重启策略（如 `no`、`on-failure`、`always`、`unless-stopped`）。
- **`-u`**: 指定用户。



## Backend : Spring boot project 

#### 修改application.properties

因為目前Mysql database從本地的mysql改為docker mysql，並且原本spring.datasource.url 的位置localhost:3306，在放入docker環境後， docker會是尋找 container 內自己網路中的mysql連接，這時會找不到。

（不同的container是相互隔離的，無法連接到不同的container，除非把他們設為同一個網路中）

透過在windows terminal 使用 "ipconfig" 找到本機的 ipv4位置，更改 spring.datasource.url 為ip位置以及 MySQL docker 的port。

```properties
spring.datasource.url=jdbc:mysql://{ip位置}:{mysql_docker_port->ex.3307}/service_booking_system_db?serverTimezone=Asia/Taipei&characterEncoding=utf-8
spring.datasource.username={your_username}
spring.datasource.password={your_password}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```



#### 打包spring boot project

在intelliJ terminal 輸入指令

```
java clean package
```

或是透過maven工具列的選項，先選clean，再選package。

此時出現target directory，目標的{projectName}.jar在裡面，透過下面指定可以執行java project

```
java -jar {projectName}.jar
```



#### 將spring boot project打包為docker image

專案根目錄建立DockerFile，簡單來說就是將bookService.jar複製到docker中並命名為app.jar，entrypoint是進入docker會執行的指令

```dockerfile
FROM openjdk:17-jdk

# copy .jar file
COPY ./target/bookService.jar app.jar

# Export the backend port
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```



執行指令在當前目錄建構docker image

```
docker build -t springboot .
```



執行docker container即可連線mysql docker 並讓前端連線， port=8080

```
docker run -d -p 8080:8080 --name springboot8080 springboot
```





## Docker Compose啟動多個服務

[docker compose](https://www.youtube.com/watch?v=6hMHziv0T2Y)

docker compose可以管理多個container，當有多個服務需要一起啟動時，能使用它完成。

### 後端：spring boot + mysql

docker-compose.yml 文件，到該檔案資料夾的路徑使用指令：***docker-compose up -d* **，就能執行docker-compose。

```yaml
version: '3.7'

networks:
  web_app:
    driver: bridge
    name: web_app_network

services:
  db:
    image: mysql:8
    container_name: mysqldb
    networks:
      - web_app
    environment:
      - MYSQL_ROOT_PASSWORD=mysql
      - MYSQL_DATABASE=service_booking_system_db
    ports:
      - "3307:3306"
    volumes:
      - ./db_data:/var/lib/mysql
      - ./database/backup.sql:/docker-entrypoint-initdb.d/backup.sql

  app:
    image: springboot
    container_name: spring-boot-app
    environment:
      - MYSQL_HOST=mysqldb   # same as container-name of db
      - MYSQL_PORT=3306
    networks:
      - web_app
    ports:
      - "8080:8080"
    depends_on:
      - db
volumes:
  db_data:
```

#### 建立一個網絡(network) 命名為 web_app

* driver: bridge：bridge 橋接模式，主要是用來對外連接的，docker container預設的網絡使用的就是bridge，允許container連接到到同一網絡上的其他container。

#### 兩個services

* db :  = mysql database

  * image使用前面Database小節pull下來的docker image，mysql:8

  * 執行後的container命名為：mysqldb，**這邊要注意：appllication.properties中的mysql url必須要設定為這個container name，並且port號要使用Mysql預設的3306才能執行。** 

    * > 補充：因為把java web 丟到 docker中執行，對於docker中的環境來說，container之前是相互隔離的，為了能在不同的容器中做不同的事情，所以在執行中只會找相同網絡中的其他容器。
      >
      > 我們創建了一個network並把兩個容器中加到同一個網絡中，所以要使用depends_on服務的容器名稱作為連線的主機名稱，而對於容器內的連線port號是原先預設的3306，雖然我們映射到了本地的3307 port號，但那個是對於本地的執行連接來說，才能使用3308=7連接。
      >
      > 也就是說，我如果要從我本機使用MySQL workbench連接這個MySQL container的話，我要使用3307才能連接的到。

  * volumes：將本地的 db_data 掛載 docker中的/var/lib/mysql 資料夾(docker中放mysql data的位置)。

  * 如果需要在docker啟動時，執行db的script建立資料庫和執行db schema，可以在docker-compose.yml的volumes把script放到docker-entrypoint-initdb.d，這樣當啟動docker的時候，就會自動初始化資料庫。這邊我們把本地的backup.sql 作為docker初始化資料庫的腳本。

* app ： =spring boot project

  * 依賴於db service，會先啟動db，再啟動app

  * MYSQL_HOST、MYSQL_PORT對應到appllication.properties中的

  * 將appllication.properties 中的 mysql url改為下面那樣

    * ```
      spring.datasource.url=jdbc:mysql://{MYSQL_HOST:localhost}:{MYSQL_PORT:3306}/service_booking_system_db?serverTimezone=Asia/Taipei&characterEncoding=utf-8
      ```

雖然depends_on 已經有配置要依賴於db服務了，但無法確保他一定會等db都完整啟動了，才啟動app，所以在第一此使用docker-compose up 執行時，會看到db有啟動成功，但app仍顯示連線失敗的錯誤訊息。

通常第一次執行後用 **docker ps -a** 指定查看，會看到只有mysql service建立起來，這時再執行一次 **docker-compose up -d**  spring boot service也能被執行起來。

如果要特別控管一定要等db執行成功，才執行app，可以寫控管流程的腳本，下面是修改後的docker-compose，主要是db中加入healthycheck，檢查db service中是否能ping到docker中的mysql。app service啟動時會檢查db service是否為healthy。

```yaml
version: '3.7'

networks:
  web_app:
    driver: bridge
    name: web_app_network

services:
  db:
    image: mysql:8
    container_name: mysqldb
    networks:
      - web_app
    environment:
      - MYSQL_ROOT_PASSWORD=mysql
      - MYSQL_DATABASE=service_booking_system_db
    ports:
      - "3307:3306"
    healthcheck:
      test: ["CMD", "mysqladmin" ,"ping", "-h", "localhost"]
      timeout: 10s  # 健康檢查命令的超時時間
      interval: 5s # 檢查間隔時間
      retries: 10   # 如果健康檢查命令失敗，Docker 將進行的重試次數，
    volumes:
      - ./db_data:/var/lib/mysql
      - ./database/backup.sql:/docker-entrypoint-initdb.d/backup.sql

  app:
    image: springboot
    container_name: spring-boot-app
    environment:
      - MYSQL_HOST=mysqldb   # same as container-name of db
      - MYSQL_PORT=3306
    networks:
      - web_app
    ports:
      - "8080:8080"
    depends_on:
      db:
        condition: service_healthy
volumes:
  db_data:
```



### 前端：Angular + NginX

首先要將Angular中使用到後端API的url從localhost改為本機實體IP位址，基本上都在各種services物件中會使用到。

Angular的部屬是將source code丟到docker中再build。

我們可先在本地端嘗試build project，執行下面指令編譯project

```bash
npm run build
# or
ng build
```

這時會看到Error：exceeded maximum budget。

> 原因：
>
> Angular會檢查應用程式以及各元件的大小，因為太大網站加載速度會減緩，Angular budgets就像設定一個上限，防止我們設計的程式太大。
>
> 修改方式：
>
> 在**angular.json** file中，找到configuration: production : budgets，修改其中所有的maximumWarning、maximumError的大小。
>
> <img src="C:\Users\joseg\AppData\Roaming\Typora\typora-user-images\image-20241113032525160.png" alt="image-20241113032525160" style="zoom:50%;" />



編譯成功後會出現dist資料夾，編譯後的project會放在那裡。

前端dockerfile要做的就是將project複製到docker中，並在docker內編譯，再透過nginx部屬編譯後的angular project。

#### .dockerignore：省略的檔案

首先設定不要放入docker的檔案(node_modules 一定不能放)，在Angular project的root資料夾新增 **.dockerignore**

檔案， **/fie 能檢查所有資料夾下面的檔案

```dockerfile
# .dockerignore file
**/node_modules/
**/.vscode
**/dist
**/.editorconfig
**/.angular
**/README.md
**/.gitignore
**/Dockerfile
**/LICENSE
**/.git
```



#### Dockerfile

分為兩個部分

* First ： docker中建立Angular project，很值觀能看到就是把project複製到docker，接著編譯
* Second： 部屬到nginx，會將第一步編譯出的 **dist/{project name}** 中的檔案複製到 **/usr/share/nginx/html** 資料夾中，這個也是nginx 設為root的位置。接著要將我們在本地端設定的nginx檔案複製到 **/etc/nginx/conf.d/default.conf**，nginx執行時參考的檔案為 **/etc/nginx/nginx.conf**，這個檔案會引入**/etc/nginx/conf.d** 資料夾中所有的**.conf** 檔案，因此我們不直接將nginx.conf覆蓋掉，而是替換掉 conf.d/default.conf 

```dockerfile
# first stage： construct Angular application
FROM node:20 AS build
# set the working directory
WORKDIR /app    
# copy all fils(without in .dockerignore) to working directory
COPY . .
# npm install and build the application
RUN npm install
RUN npm run build

# second stage： Deploy to NginX
FROM nginx:stable-alpine
# from first stage. copy the dist file in build stage to 
COPY --from=build /app/dist/servie-book-sys-web /usr/share/nginx/html
# copy local nginx.conf to replace the default.conf
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
```



#### 本地端編寫的 nginx.conf

新增nginx.conf。其中的root 對應到Dockerfile第二階段中從Angular/dist 複製過去的目錄，index為Angular編譯出的index.html。

```
server {
    listen 80;

    # 靜態文件的根目錄
    location / {
        root /usr/share/nginx/html;
        index index.html index.htm;
        try_files $uri $uri/ /index.html; # 配置 SPA 的路由，不然網頁重新整理會報錯
    }

    # API 請求代理設定
    location /api/ {
        proxy_pass http://192.168.0.103:8080/;  # backend_host_ip 是後端服務的 IP
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

這邊只是簡單範例，Ningx還能達到附載平衡，減低記憶體消耗，轉換成https等功能

[參考](https://medium.com/starbugs/web-server-nginx-2-bc41c6268646)

[參考](https://ithelp.ithome.com.tw/articles/10280840)

[proxy_pass對應方法](https://ithelp.ithome.com.tw/articles/10280840)



#### docker-compose 執行

透過docker-compose執行dockerfile，在當前文件目錄使用**docker-compose up -d --build** 執行。

```yaml
version: '3.7'
services:
  frontend:
    container_name: frontend
    build:
      context: .
      dockerfile: Dockerfile
    image: book_service_frontend
    ports:
      - "80:80"
    networks:
      - frontend_network

networks:
  frontend_network:
    driver: bridge
```





## 參考

建立後端docker環境：[參考](https://ithelp.ithome.com.tw/users/20162058/ironman/6809?page=1)


