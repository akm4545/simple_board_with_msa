package com.replyservice.replyservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication
//액추에이터가 생성해주는 /refresh 엔드포인트로 컨피그 서버의 데이터 갱신
//DB 접속정보는 갱신되지 않는다
//application.yml에서 액추에이터 endpoint를 노출시켜야 한다
@RefreshScope
public class ReplyServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReplyServiceApplication.class, args);
    }

}
