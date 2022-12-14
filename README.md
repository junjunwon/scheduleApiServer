# scheduleApiServer

## 서버 구동을 위한 환경 설정
### 1. Amazon-Corretto JDK 17 설치
#### 1) JDK 설치 경로
- Windows x64 : https://corretto.aws/downloads/latest/amazon-corretto-17-x64-windows-jdk.msi
- macOS x64 : https://corretto.aws/downloads/latest/amazon-corretto-17-x64-macos-jdk.pkg
#### 2) Jdk 설치 확인
- Windows x64 : Cmd 실행 -> Java -version
- macOS x64 : Terminal 실행 -> Java -version

### 2. IntelliJ IDE 실행
#### 1) 아래 그림과 같이 설정값을 추가해준다.
![springbootConfiguration](https://user-images.githubusercontent.com/45681336/186893113-932779c2-f9bc-41e6-abdd-c32d3f0235ed.png)

#### 2) vm options 설정 ipv6 -> ipv4로 변환
- -Djava.net.preferIPv4Stack=true 
- -Djava.net.preferIPv4Addresses=true

### 애플리케이션 서버를 구동한다.

![applicationExecute](https://user-images.githubusercontent.com/45681336/186894067-a6a75201-3b89-4697-8020-b9d8f21ab90d.png)
![H2_url](https://user-images.githubusercontent.com/45681336/186893839-91474d5c-8072-4d2f-aac7-f9569168d999.png)


## 실행 테스트
### 초기 로그인을 위한 init 계정 정보
- username : admin
- password :P@ssw0rd

### 계정 생성
**등록 : /api/createUser**
- 아래와 같이 ROLE_GUEST로 등록된 사용자는 권한 부족으로 로그인을 시도할 경우 거부된다. (로그인 가능 사용자 : ROLE_USER)
```
userInfo : {
  "username" : "guest",
  "password" : "P@ssw0rd",
  "role" : "ROLE_GUEST"
}
```
### 파일 CRUD 방법 - URL 
**조회 : /api/fileList/list**
- Bucket4j를 활용한 rate limit check
  - availableTokens 횟수만큼 request 가능하다.
    - availableTokens는 response data로 확인 가능하다.
  
**등록 : /api/file/save**
```
{
    "time" : "2022-05-25T13:25:01",
    "joinMemberCnt" : 3,
    "leaveMemberCnt" : 11,
    "payment" : 59156,
    "cost" : 75000,
    "revenue" : 90000
}
```
**수정 및 유효성 검증 : /api/file/update/{id}**
- value가 0보다 클 경우 수정 및 정상 Return 
- value가 0보다 작을 경우 errorResponse 호출
```
{
    "joinMemberCnt" : 75,
    "leaveMemberCnt" : -1,
    "payment" : 59156,
    "cost" : 75000,
    "revenue" : 90000
}
```
삭제 : /api/file/delete/{ids}
- {ids}에 해당하는 row값 삭제.

### POSTMAN 파일 CRUD 방법
- Postman request json 파일 
  - https://drive.google.com/file/d/1H2BrRw8jB3xfH_1VdW3-3lXqFAHDXrU3/view?usp=sharing
- Postman으로 API 요청 시 아래 그림과 같이 Authorization의 Basic Auth에 권한을 넣고 API를 요청한다. 
- <img width="908" alt="스크린샷 2022-08-29 오후 8 49 13" src="https://user-images.githubusercontent.com/45681336/187194618-00765f35-5399-4425-b2db-c0e92ba90f2d.png">
### 로그 파일 위치 및 로그 정보
- {root 경로}/logs/ 경로에 로그 파일이 생성된다.
- 로그 파일 규칙
  - {프로세스 명}.log : 현재 로그가 저장되는 파일
  - {프로세스 명}_yyyy-MM-dd-hh.log : 1시간 경과 후 날짜 pattern이 추가되어 저장된다.
- 로그 파일명
  - fileInfo.log : Request 시간, Response 시간, 소요 시간에 대한 파일 읽기 및 쓰기 관련 로그 파일
  - userInfo.log : Request 시간, Response 시간, 소요 시간에 대한 계정 관련 로그 파일
  - log.log : 일반 로그 파일