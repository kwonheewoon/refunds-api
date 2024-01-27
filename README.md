# 세금환급 조회 API : 권희운

## 사용 기술 스택 : java, Spring boot, jpa, Embedded H2 DB, Spring Security

## 빌드 방법
WORK_DIR :  refunds-api

jar 파일 빌드 (테스트코드 제외) \
./gradlew build -x test


## 서버 구동 방법
빌드된 jar 파일 실행 \
java -jar ./build/libs/refunds-api-0.0.1-SNAPSHOT.jar


## 구현방법
1.유저 가입시 juser_join_access에 있는 정보와 매칭하여 가입 가능한 유저 및 중복가입 체크 \
2.유저 로그인시 유저 정보 토대로 JWT 토큰 생성
3.SecurityContextHolder에 JWT 토큰의 정보 저장(Authentication 변환)
3.JWT 토큰을 Authorization 헤더에 실어 API 요청
4.SCRAP API 요청시 RestClient를 사용하여 SCRAP 데이터를 받아온다.


## API 사용 가이드
### Swagger Ui : localhost:8080/swagger-ui/#/user-rest-controller
Swagger Authorization 값 세팅방법 \
ㄴ Authorization : {토큰 값}




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

