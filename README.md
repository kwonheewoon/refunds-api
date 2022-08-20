# 유저 환급액 계산 API : 권희운

## 사용 기술 스택 : java, spring boot, jpa, QueryDsl, Embedded H2 DB, Spring Security

## 빌드 방법
WORK_DIR :  refunds-apk

jar 파일 빌드 (테스트코드 제외) \
./gradlew build -x test


## 서버 구동 방법
빌드된 jar 파일 실행 \
java -jar ./build/libs/refunds-api-0.0.1-SNAPSHOT.jar


## 단위, 통합테스트
Service, Repository 단위테스트 \

## 구현방법
1.유저 로그인시 SecurityContextHolder에 인증 정보 저장
2.SecurityContextHolder에 담겨있는 인증정보를 토대로 JWT 토큰 생성
3.JWT 토큰을 Authorization 헤더에 실어 API 요청
4.SCRAP API 요청시 Webclient를 사용하여 SCRAP 데이터를 받아온다.


## API 사용 가이드
### Swagger Ui : localhost:8080/swagger-ui/#/user-rest-controller

### API Status Code
    /*
     * 200010 : 유저 정보가 정상적으로 등록 되었습니다.
     */

    /*
     * 200011 : 유저 정보가 정상적으로 등록 되지 않았습니다. (필수 요청값을 확인해 주세요.)
     */

    /*
     * 200020 : 로그인이 정상적으로 완료 되었습니다.
     */

    /*
     * 200030 : 유저 정보가 정상적으로 조회 되었습니다.
     */

    /*
     * 200040 : 유저의 세무정보 스크랩이 완료되었습니다.
     */

    /*
     * 200050 : 환급액 계산이 완료되었습니다.
    
    /*
     * 400020 : 로그인 정보가 잘못 되었습니다.
     */

    /*
     * 400010 : 유저 가입이 불가능한 정보 입니다.
     */

    /*
     * 400020 : 로그인 정보가 잘못 되었습니다.
     */

    


### 유저 가입 API
POST : http://localhost:8080/szs/signup
```c
body : {
  "name": "홍길동",
  "password": "123456",
  "regNo": "860824-1655068",
  "userId": "hong123"
}
```

### 로그인 및 토큰 발급 API
POST : http://localhost:8080/szs/login
```c
body : {
  "password": "123456",
  "userId": "hong123"
} 
```

### 유저 정보 조회 API
GET : http://localhost:8080/szs/me

```c
header : Authorization = JWT 토큰
body : {} 
```


### 유저 세무정보 scrap API
POST : http://localhost:8080/szs/scrap

```c
header : Authorization = JWT 토큰
body : {}
```


### 유저 환급액 계산 및 조회 API
GET : http://localhost:8080/szs/refund

```c
header : Authorization = JWT 토큰
body : {}
```

