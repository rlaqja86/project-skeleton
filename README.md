#Environment (Tech stack)
Spring Boot,
Spring security
MySql,
Redis(embedded),
JWT,
Spock,
#How to Run
root 디렉토리에 sh deploy.sh 로 jar 로컬에 배포

혹은 IntelliJ에 Project Import 후 Application실행
#Design

###SMS인증

1. 사용자가 핸드폰 번호로 인증 요청을 보냅니다. (SMSAuthController)

2. 실제 SMS 메시지를 보내는 부분은 구현되어있지 않습니다. 대신 SMS 코드를 생성하고 redis에 키를 사용자의 핸드폰 번호, 값은 SMS코드를 저장후 만료시간을 60초로 설정합니다.

3. SMS인증 주기동 사용될 JWT토큰을 발급합니다.

4. (실제로는 SMS로 코드를 받겠지만) SMS코드와 accessToken을 Response로 받습니다.

5. 유저는 코드가 유효한 1분내 발급받은 accessToken과 SMS코드로 최종 인증 시도를 보냅니다. 이때 회원 가입이나 비밀번호 교체 등 원하는 Command도 같이 보냅니다. 해당 command에 따라 nextUrl이 command에 맞 제공됩니다.

6. accessToken이 유효하고 redis에 요청이 온 phoneNumber에 해당하는 sms코드가 있다면 이를 비교합니다. 하지만 코드가 유효하지 않거나 1분이 지나 만료된 경우 예외를 발생시킵니다.

7. 만약 코드가 일치한다면 회원 가입 URL이 포함된 url이 포함된 response를 보냅니다.
 
###회원가입

1. SMS 인증으로 받은 URL로 회원 가입 요청을 보냅니다. 요소로는 이메일, 비밀번호, 핸드폰번호, 이름, 닉네임, 그리고 아까 발급받은 accessToken이 있습니다. 여기서 로그인으로 사용되는 정보는
email과 password 입니다.

2. accessToken이 유효하면 기존에 가입된 email이 있는지 DB에서 찾습니다. 만약 동일한 Email이 존재한다면 duplicate 예외를 던집니다.

3. 중복된 email이 존재하지 않는다면 DB에 해당 정보를 entity로 전환하여 저장합니다.


###로그인

Spring Securty + JWT 토큰 인증으 로그인 유효성을 검사합니다.

1. 로그인 요청이 오면 요청의 ID, Password 기반으로 
AbstractAuthenticationToken를 구현한 CustomEmailPasswordToken을 생성합니다. 

2. 해당 토큰 Spring Security - AuthenticateManager의 authenticate 의 인자로 넘겨 ID PW의 유효함을 검사합니다. 

3. AuthenticateManager는 Provider들 중 matching되는 Provider를 찾는데, CustomEmailPassowrdToken을 처리하는 EmailPasswordAuthProviderImpl을 미리 구현해 놓았기 때문에 
해당 EmailPasswordAuthProviderImpl에서 DB에 있는 Encoding된 패스워드 정보와 유저가 요청한 password를 인코딩하여 비교합니다.

4. 만약 ID와 Password가 올바르지 않다면 EmailPasswordAuthProviderImpl에 예외가 던져지고, BaseController에서 알맞은 exceptionHandler에서 예외를 캐치하여 
처리됩니다.

5. 올바른 ID와 Password 라면 JWT token을 생성하여 UserDto와 함께 Response로 제공합니다.

6. 그럼 앞으로 Client는 해당 JWT Token을 accessToken으로 사용, 헤더에 포함해 인증을 대신할 수 있게 됩니다.

7. 일정 시간이 지나 accessToken이 expired되었을때 client는 refreshToken을 사용해 새 accessToken을 받습니다. refreshToken의 만료기간은 1주로 매우 깁니다.
 
###인증

1. 로그인 시 받은 AccessToken을 사용합니다. AccessToken을 Authorization필드에 Bearer 타입으 넣어 /auth/가 포함되지 않은 모든 api 요청에 사용됩니다.

2. auth가 들어가지 않은 매 요청마다 OncePerRequestFilter의 구현체인 JwtAuthenticationFilter를 거칩니다. 해당 필터에서는 jwt토큰의 유효성을 검사합니다.

3. 만약 토큰이 request header에 존재하고 유효하다면 SecurityContext에 CustomEmailPasswordToken을 인증 완료된 상태로 생성하여 set을 수행합니다.

4. Token이 존재하지 않거나 valid하지 않은 경우 접근이 차단됩니다.

비밀번호 변경

1. 위 SMS 인증 절차를 따릅니다. 최종적으로 비밀번호 변경 url과 accessToken을 받습니다.

2. 해당 토큰으로 변경을 원하는 password가 담긴 DTO를 요청으로 보냅니다.

3. 해당 User를 DB에서 찾은 후 password를 encode하여 새로 저장합니다.

#구현 내용

Spring Security와 JWT토큰을 이용한 인증 정책에 구현을 집중했습니다. 기본적으로 accessToken과 refreshToken으로 인증이 이뤄집니다. 최초 유저가 로그인 후
accessToken과 RefreshToken이 제공되며 accessToken으로 인증을 진행하다가 토큰이 만료된 경우 refreshToken으로 valid함을 확인하고 다시 accessToken을 받아
로그인을 유지하는 정책을 적용했습니다. 

accessToken 재발행의 경우 refreshToken을 검사 후 accessToken을 재 발행하는 로직만 구현되어있고. accessToken이 만료됨을 판단하는 것은 client에 위임하는 것으로 가정했습니다.

SMS로 문자를 보내는 부분은 구현되어 있지 않습니다. 대신 테스트를 용이하게 하기 위해 최초 번호 인증시 response에 토큰과 더불어 sms코드를 보냅니다.


#요청 샘플

####get SMS Code (POST http://localhost:8080/api/v1/auth/sign-up/reset/password)

curl -X POST http://localhost:8080/api/v1/auth/sms?phoneNumber=01049249971

####verify SMS Code (POST http://localhost:8080/api/v1/auth/sms/code)

curl --header "Content-Type: application/json" --request POST --data '{
"signUpNextUrl":"kim beom",
"accessToken":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIwMTA0OTI0OTk3MSIsImlhdCI6MTY3MDE0Nzg1NywiZXhwIjoxNjcwMTQ4NDU3fQ.s4BxjdrGB3LVkUmsM6j6l7cNW1XlKmLKyPXZOX1mQBY", 
"smsVerificationCode" : "0451", 
"command" : "REGISTRATION"
}' localhost:8080/api/v1/auth/sms/code
                                                                      
                                                      
#### sign up (POST http://localhost:8080/api/v1/auth/sign-up/registration)

curl --header "Content-Type: application/json" --request POST --data '{
"email":"k1b219@naver.com",
"nickName":"beom",
"password" :"1234",
"name":"beom",
"phoneNumber":"01049249971",
"tokenDto": {
    "accessToken" : "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIwMTA0OTI0OTk3MSIsImlhdCI6MTY3MDE0Nzg1NywiZXhwIjoxNjcwMTQ4NDU3fQ.s4BxjdrGB3LVkUmsM6j6l7cNW1XlKmLKyPXZOX1mQBY",
    "refreshToken" : ""
}
}' localhost:8080/api/v1/auth/sign-up/registration

 
#### change password (POST http://localhost:8080/api/v1/auth/sign-up/reset/password)

curl --header "Content-Type: application/json" --request POST --data '{
"email":"",
"nickName":"",
"password" :"newpassword",
"name":"",
"phoneNumber":"01049249971",
"tokenDto": {
    "accessToken" : "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIwMTA0OTI0OTk3MSIsImlhdCI6MTY3MDE0NTUwOSwiZXhwIjoxNjcwMTQ2MTA5fQ.AdObvV5y9VnedMlxehZ_Khvoq3UF_sA3sDi5s1Vt9NM",
    "refreshToken" : ""
}
}' localhost:8080/api/v1/auth/sign-up/reset/password


#### login (POST http://localhost:8080/api/v1/auth/login)

curl --header "Content-Type: application/json" --request POST --data '{
"email":"k1b219@naver.com",
"password" :"1234"
}' localhost:8080/api/v1/auth/login

#### token reissue (GET http://localhost:8080/api/v1/auth/token/reissue)

curl --request GET localhost:8080/api/v1/auth/token/reissue?refreshToken=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrMWIyMTlAbmF2ZXIuY29tIiwiaWF0IjoxNjcwMTQyOTk0LCJleHAiOjE2NzA3NDc3OTR9.D_3jGYf4YuTvdLBMGVile426rq8BdHcw1hKgPeG9lyw

### get user info (GET http://localhost:8080/api/v1/user)

curl --header "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrMWIyMTlAbmF2ZXIuY29tIiwiaWF0IjoxNjcwMTQ4MzMyLCJleHAiOjE2NzAxNDg5MzJ9.lswEp1dSbwVAv7DiBq4hKgyNvaAysTcgmzAiELoXWbw" --request GET localhost:8080/api/v1/user?email=k1b219@naver.com

#### Test (GET http://localhost:8080/api/v1/hello)

curl --header "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrMWIyMTlAbmF2ZXIuY29tIiwiaWF0IjoxNjcwMTQyOTk0LCJleHAiOjE2NzA3NDc3OTR9.D_3jGYf4YuTvdLBMGVile426rq8BdHcw1hKgPeG9lyw" --request GET localhost:8080/api/v1/hello

