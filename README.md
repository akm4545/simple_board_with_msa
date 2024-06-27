---------------------------------------------------------------------------- MSA ----------------------------------------------------------------------------
1. 백엔드 프로젝트를 도메인별로 나눈 뒤 롬복, 스프링 액추에이터, 스프링 웹 의존성을 추가하여 생성
2. JPA 의존성을 추가하고 기초 CRUD 토대를 만든다
3. DB를 생성하고 통신 테스트를 진행한다
4. HATEOAS 의존성 추가
  HATEOAS = HATEOAS원칙(해당 리소스와 관련된 링크를 표시하는)을 준수하는 API를 생성
  각 서비스의 응답과 함께 가능한 다음 단계 정보도 제공 클라이언트를 다음 단계로 가이드
5. 컨피그 서버를 구축한다 (아래 의존성 추가하여 프로젝트 생성)
  - Spring boot Actuator
  - Spring Colud Config Server
6. 컨피그 서버에서 /src/main/resource 폴더에 application.yml 파일 생성
7. 컨피그 서버 Application 클래스에 @EnableConfigServer 추가
8. application.yml 작성 /src/main/resource/config 에 프로젝트명-프로필명.yml 파일을 만들고 작성 후 프로젝트 구동한 다음 http://localhost:8071/프로젝트명/프로필명을 실행하여
  정상 작동하는지 테스트
9. 구동 서비스들에게 spring cloud 의존성을 주입한다
10. 서비스 서버들의 applicaiton.yml에 config 서버 정보를 기입한다
11. 서비스들의 Application 클래스 상단에 @RefreshScope 어노테이션을 붙여 컨피그 서버에서 정보가 바뀌었을때 재기동 없이 액추에이터가 생성한 /refresh 엔드포인트로 새로고침 하도록 한다
    스프링 데이터에 사용되는 데이터베이스 구성 정보와 같은 항목은 이 어노테이션으로 갱신되지 않는다
12. application.yml 에서 액추에이터 설정을 통해 refresh 엔드포인트를 노출시키는 설정을 넣어야 한다
13. 컨피그 서버에서 데이터 암호화 키를 넣고 민감정보를 암호화 한다 (/encrypt 엔드포인트로 암호화할 데이터 넣고 요청) 
14. 암호화된 데이터는 "{chiper}암호화된 데이터" 형식으로 입력해야 한다
15. 서비스 디스커버리 서버 생성
    서비스 디스커버리 = 서비스의 물리적 위치를 수동으로 구성할 필요 없이 위치를 알려 줄 수 있는 아키텍처 구축
    - Spring boot Actuator
    - Spring cloud starter config
    - Spring cloud starter netflix eureka server
    - ribbon-eureka
    - spring cloud starter loadbalancer
16. 컨피그 서버에 서비스 디스커버리 서버의 설정 파일을 작성한다
17. 서비스 디스커버리 서버에 유레카 설정 yml 작성 후 @EnableEurekaServer 어노테이션을 Application 클래스에 선언하여 유레카 서비스를 활성화한다
18. 서비스들에 eureka-client 의존성을 추가
19. 컨피그 서버의 서비스들 application.yml 파일에 유레카 클라이언트 설정 정보를 넣는다
20. 컨피그 서버 -> 유레카 서버 -> 서비스 서버 순으로 구동 후 유레카에 서비스가 등록될때까지 (30초) 기다린 후
  http://<eureka service>:<port>/eureka/apps/<APPID> ex) http://localhost:8070/eureka/apps/user-service
  http://<eureka service>:<port>
  를 호출해서 정상 등록되었는지 확인한다
21. 서비스 디스커버리에 등록된 url을 가져오기 위해 서비스들에 feign 의존성을 추가하고 Application 클래스에 @EnableFeignClient 어노테이션을 추가한다
22. 서비스들에 필요한 FeignClient 인터페이스를 정의한다.
23. 현재 프로젝트에서는 board-service와 reply-service가 외부 서비스가 필요하므로 해당 서비스들에만 정의
24. 클라이언트 회복성을 위한 Resilience4j (마이크로서비스 회복성 패턴 구현) 의존성을 서비스들에 추가
   - resilience4j
   - spring-boot-starter-aop
25. 서비스에 회로 차단기 패턴 구현
   - 회로 차단기: 원격 호출을 모니터링하고 서비스를 장기간 기다리지 않게 한다 이때 회로 차단기는 연결을 종료하고 더 많이 실패하며 오작동이 많은 호출이 있는지 모니터링하는 역할을 한다
   - 그런 다음 이 패턴은 빠른 실패를 구현하고 실패한 원격 서비스에 추가로 요청하는것을 방지한다
   - 서비스들의 서비스 클래스 메소드 위에 @CircuitBreaker(name="이름") 어노테이션을 달아준다 추후 이름은 서킷브레이커 커스텀 yml에서 사용된다
26. 컨피그 서버의 설정 yml에 회로차단기 패턴 설정값을 넣는다 값에 대한 설명은 해당 설정파일에 주석처리
27. 회로 차단기 패턴에 폴백 처리를 추가한다
   - 회로 차단기 패턴의 장점 중 하나는 이 패턴이 중개자로 원격 자원과 그 소비자 사이에 위치하기 때문에 서비스 실패를 가로채 다른 대안을 취할 수 있다
   - Resilience4j에서 이 대안을 폴백 전략 이라고 한다
   - @CircuitBreaker 어노테이션에 fallbackMethod 속성을 추가하고 폴백 메서드를 만든다
28. 벌크헤드 패턴 구현
   - A, B, C 자원이 있고 C 자원에 요청이 몰려 모든 쓰레드를 가져다 쓴다고 했을 때 A, B의 접근 시도는 쓰레드가 반환될때까지 후순위로 대기한다
   - 결국 자바 컨테이너는 멈추게 되고 장애가 전파된다
   - 벌크헤드 패턴은 각 자원에 대한 스레드를 각각 격리하고 할당한다
   - Resilience4j는 세마포어 벌크헤드 방식으로 격리하며 분리된 스레드 풀로 나누어져 있다
29. 컨피그 서버의 yml 파일에 벌크헤드 패턴 설정값을 넣어준다 값에 대한 설명은 해당 설정파일에 주석처리
   - 사용자에게 맞는 스레드 풀을 구하는 공식
   - (서비스가 정상일 때 최고점(peak)에서 초당 요청 수 * 99 백분위수(P99) 지연 시간(단위: 초)) + 부하를 대비해서 약간의 추가 스레드
   - 스레드 풀 프로퍼티를 조정해야 하는 주요 지표는 대상이 되는 원격 자원이 정상인 상황에서도 서비스 호출이 타임아웃을 겪고 있을 때
30. 벌크헤드 패턴을 적용할 메서드 위에 @Bulkhead(name="이름" fallbackMethod="풀백 메서드")어노테이션을 추가한다.
   기본 방식은 세마포어 방식이며 스레드 풀 방식으로 변경하려면 type=Bulkhead.Type.THREADPOLL을 추가한다
31. 재시도 패턴 구현
   - 서비스가 처음 실패했을 때 서비스와 통신을 재시도하는 역할을 한다
   - 고장(네트워크 장애 같은)이 나도 동일한 서비스를 한 번 이상 호출해서 기대한 응답을 얻을 수 있는 방법을 제공
   - 서비스 인스턴스에 대한 재시도 횟수와 재시도 사이에 전달하려는 간격을 지정해야 한다
32. 컨피그 서버에 재시도 패턴 설정값 기입 값에 대한 설명은 해당 설정파일에 주석처리
33. @Retry(name='', fallbackMethod='') 형식으로 어노테이션을 붙여 재시도 패턴을 적용한다
34. 속도제한기 패턴 -> 벌크헤드 패턴을 사용하므로 미사용
   - 주어진 시간 프레임 동안 총 호출 수를 제한할 수 있다 (y초마다 x개의 호출 허용)
   - 동시 횟수를 차단하고 싶다면 벌크헤드가 최선이지만 특정 기간의 총 호술 수를 제한하려면 속도 제한기가 더 낫다
   - 두 시나리오 모두 검토하고 있다면 이 둘을 결합할 수도 있다
35. 게이트웨이 서버 구축
   - 게이트웨이는 리버스 프록시(자원에 도달하려는 클라이언트와 자원 사이에 위치한 중개 서버) 이다 
   - 마이크로서비스를 호출하기 위해 유입되는 모든 트래픽의 게이트키퍼 역할
   - 서비스 게이트웨이가 있으면 서비스 클라이언트는 각 서비스 URL을 호출하지 않고 서비스 게이트웨이에 호출
   - 서비스 게이트웨이는 클라이언트와 개별 서비스의 호출 사이에 있기 때문에 서비스를 호출하는 중앙 정책 시행 지점 역할도 한다
   - 이 지점을 사용하면 각 개발 팀이 서비스 횡단 관심사를 구현하지 않고 한곳에서 수행 가능
   - 게이트웨이를 사용하여 엔드포인트가 하나가 되고 서비스 인스턴스 추가 / 제거가 쉽다
   - 필요 의존성
   - actuator
   - spring-cloud-starter-config
   - spring-cloud-starter-gateway
   - eureka
   - spring-cloud-starter-loadbalancer
36. 게이트웨이 서버의 yml 작성 후 컨피그 서버에 게이트웨이 서버 유레카 구성 정보 yml를 추가한다
37. 게이트웨이 서버의 Application 클래스에 @EnableEurekaClient 어노테이션을 선언한다 (스프링 클라우드 2022.0 이상 버전에서는 선언할 필요가 없다)
38. 컨피그 서버의 게이트웨이 yml 파일에 서비스 디스커버리를 이용한 자동 경로 매핑 설정을 추가해준다 
                               서비스 이름은 서비스의 물리적 위치를 검색하는데 서비스 게이트웨이의 키 역할 - 경로의 나머지 부분은 실제로 호출될 URL 엔드포인트
   - 자동 경로 매핑 요청 예시 -> http://localhost:8072(게이트웨이 주소)/user-service(호출 서비스 명)/user/list(실제로 호출될 URL 엔드 포인트)
39. 게이트웨이 서버 구동 후 actuator/gateway/routes(관리 경로 목록) 엔드포인트를 통해 잘 작동하는지 테스트
40. 게이트웨이 라이팅 정보가 변경되었다면 post /actuator/gateway/refresh 엔드포인트로 요청하면 설정이 바뀐다
41. 상관관계 ID 설정을 위한 횡단 관심사를 Predicate 및 Filter Factories를 사용하여 구현
   - 하단에 작성한 3가지 사용자 정의 필터를 생성
   - 추적 필터: 게이트웨이 진입 시 상관관계 ID가 있는지 확인하는 사전 필터 (마이크로 서비스를 통과할 때 방생하는 이벤트 체인을 추적하기 위한 용도)
   - 대상 서비스: HTTP 요청 헤더에서 상관관계 ID를 받는다
   - 응답 필터: 상관관계 ID를 클라이언트에 전송될 HTTP 응답 헤더에 삽입하는 사후 필터 이 방식으로 클라이언트도 요청과 연결된 상관관계 ID에 엑세스 할 수 있다
42. 게이트웨이에 사전 필터를 만든다
   - tmx-correlation-id가 HTTP 헤더에 없으면 사전 필터가 상관관계 ID를 생성하고 설정
   - 있다며 아무일도 하지 않는다 (상관관계 ID가 있다는 것은 이 특정 서비스 호출이 사용자 요청을 수행하는 서비스 호출 체인의 한 부분임을 의미한다)
   - 모든 필터에서 사용할 기능을 캡슐화한 FilterUtils 클래스를 만든다 (해당 기능의 구현은 자유롭게)
   - TrackingFilter 클래스를 생성하고 필터 내용을 작성한다
43. 각 서비스에 UserContextFilter, UserContext, UserContext Interceptor 세 가지 클래스를 생성한다
   - 이 클래스는 유입되는 HTTP 요청의 상관관계 ID를 읽기 위해 협업하고 애플리케이션의 비즈니스 로직에서 쉽게 액세스하고 사용할 수 있는 클래스에 ID를 매핑해서
   - 모든 하위 서비스 호출에 전파
   - UserContextFilter = 유입되는 HTTP 요청을 가로채고 HTTP 요청에서 사용자 컨텍스트 클래스로 상관관계 ID(외 몇가지 정보)를 매핑하는 필터
   - UserContext = 각 서비스 크라이언트 요청의 HTTP 헤더 값을 보관
   - UserContextInterceptor = HTTP 기반 서비스 발신 요청에 상관관계 ID를 주입
44. 서비스들의 Application 클래스에 @LoadBalanced 어노테이션을 붙인 RestTemplate 빈을 만들어 인터셉터를 등록한다
45. 게이트웨이에 사후 필터를 전달하여 상관관계 ID를 클라이언트로 전달하고 다시 사용하도록 게이트웨이에 필터를 등록한다
46. 키클록 설치
   - 서비스와 애플리케이션을 위한 ID 및 엑세스 관리용 오픈 소스 솔루션
   - 키클록의 주요 목표는 서비스와 애플리케이션의 코딩을 전혀 또는 거의 하지 않고 서비스와 애플리케이션을 쉽게 보호하는 것
47. 키클록 docker-compose.yml 작성
48. 작성한 compose 파일로 컨테이너 구동 후 http://keycloak:8080/auth로 들어간다
   - 관리자 콘솔 메뉴를 클릭한다
   - 아이디 패스워드 입력 후 접속한다
   - 드롭다운 메뉴에서 Add realm 항목을 클릭해서 realm(키클록이 사용자, 자격증명, 역할, 그룹을 관리하는 객체를 참조하는 개념)을 추가한다
   - realm 명을 입혁하고 create 버튼을 누른다
49. 클라이언트 애플리케이션을 등록한다
   - 클라이언트 = 사용자 인증을 요청할 수 있는 개체
   - Clients 메뉴를 선택한다
   - 오른쪽 상단에 Create 버튼을 누른다
   - Client ID = 클라이언트 명, Client Protocol = poenid-connect를 선택하고 save 버튼을 눌러 저장한다
   - 액세스 타입(Access Type) = Confidential
   - 서비스 계정 활성화(Service Accounts Enabled) = On
   - 권한 부여 활성화(Authorization Enabled) = On
   - 유효한 리다이렉트 URIs(Valid Redirect URIs) = http://localhost:8081/*
   - 웹 오리진(Web Origin) : *
50. 클라이언트 역할 설정을 위해 Roles 메뉴를 선택한다
   - Add Role 버튼을 클릭한다
   - 설정할 Role 이름을 넣고 저장한다.
51. 사용자 구성
   - Users 항목 클릭
   - 양식을 입력 후 저장
   - Credentials 탭을 클릭 후 사용자 페스워드 입력
   - Temporary 옵션을 비활성화한 후 Set Password 버튼을 누른다
   - Role Mappings 탭 클릭
   - Role을 지정하자
52. 인증 서비스 시작
   - 왼쪽 메뉴에서 Realm Settings 항목 선택 -> OpenID Endpoint Configuration 링크를 클릭하여 가용 엔드포인트 목록 확인
   - Post맨 요청 테스트
   - 요청 End Point = http://keycloak:8080/auth/realms/렐름 명/protocol/openid-connect/token
   - Authorization = Basic Auth -> username = 클라이언트 ID, password = 시크릿키(client -> credentials에 있다)입력
   - body = x-www-form-unlencoded
   - grant_type = password
   - username = 유저명
   - password = 패스워드
53. 서비스들에 키클록 스프링 의존성과 스프링 시큐리티 스타터 의존성을 추가한다
   - keycloak-spring-boot-starter
   - spring-boot-starter-security
54. 컨피그 서버에 키클록 관련 설정을 입력한다
55. KeycloakWebSecurityConfigurerAdapter 클래스를 확장하여 SecurityConfig를 만든다 (spring 3 버전으로 바뀌면서 작동 x)
   - keycloak 관련 파일은 주석으로 keycloak 표기
   - client에 user, client 관련 룰 추가 필요
   - 실 서버 사용시 db 연동 필요
   - 키클록 요청 URL에 auth가 들어간 버전이랑 빠진 버전이 존재
56. 각 서비스를 보호하고 토큰의 검증을 위한 키클록 설정을 컨피그 서버의 yml에 작성한다
57. 각 서비스에 스프링 시큐리티와 키클록의 의존성을 추가한다.
58. 각 서비스에 KeycloakWebSecurityConfigurerAdapter 클래스를 확장하여 SecurityConfig를 만든다 (spring 3 버전으로 바뀌면서 작동 x)
   - 접근 제한은 해당 컨트롤러에 @RolesAllowed({"USER"}) 같은 형식으로도 가능하다
59. 상관관계 전파를 위해 작성한 클래스를 이용하여 토큰을 전달한다
