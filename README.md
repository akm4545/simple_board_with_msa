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
