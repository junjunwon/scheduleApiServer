# scheduleApiServer

## jar을 활용한 서버 구동
- https://drive.google.com/drive/folders/1isYRgIflOl1mjKmLNDbgtLSvEzb9uKLN?usp=sharing
  - 내부에 postman request 목록과 실행 가능한 jar파일이 첨부되어 있습니다.
- java -jar -Djava.net.preferIPv4Stack=true -Djava.net.preferIPv4Addresses=true scheduleApiServer-0.0.1-SNAPSHOT.jar

## 서버 구동을 위한 환경 설정
### 1. JAVA JDK 설치
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
- 
![applicationExecute](https://user-images.githubusercontent.com/45681336/186894067-a6a75201-3b89-4697-8020-b9d8f21ab90d.png)
![H2_url](https://user-images.githubusercontent.com/45681336/186893839-91474d5c-8072-4d2f-aac7-f9569168d999.png)


## 실행 확인
### 초기 계정 정보
- username : admin
- password :P@ssw0rd
### 계정 생성 및 조회 방법
등록 : /createUser
### 파일 CRUD 방법 - URL 
조회 : /fileList/list
등록 : /file/save
수정 : /file/update/{id}
삭제 : /file/delete/{ids}
### 파일 CRUD 방법 - POSTMAN
- 위 드라이브 참조 (포스트맨.json)
### 로그 파일 위치 및 로그 정보
- {root 경로}/logs/ 경로에 로그 파일이 생성된다.
- 로그 파일 규칙
  - {프로세스 명}.log : 현재 로그가 저장되는 파일
  - {프로세스 명}_yyyyMMddhh.log : 1시간 경과 후 날짜 pattern이 추가되어 저장된다.
- 로그 파일명
  - fileInfo.log : Request 시간, Response 시간, 소요 시간에 대한 파일 읽기 및 쓰기 관련 로그 파일
  - userInfo.log : Request 시간, Response 시간, 소요 시간에 대한 계정 관련 로그 파일
  - log.log : 일반 로그 파일

