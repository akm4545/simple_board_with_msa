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
  
