package com.boardservice.boardservice;

import com.boardservice.boardservice.utils.UserContextInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@SpringBootApplication
//액추에이터가 생성해주는 /refresh 엔드포인트로 컨피그 서버의 데이터 갱신
//DB 접속정보는 갱신되지 않는다
//application.yml에서 액추에이터 endpoint를 노출시켜야 한다
@RefreshScope
@EnableFeignClients
public class BoardServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoardServiceApplication.class, args);
    }

    @LoadBalanced
    @Bean
    public RestTemplate getRestTemplate() {
        RestTemplate template = new RestTemplate();
        List interceptores = template.getInterceptors();

        if(interceptores == null){
            template.setInterceptors(Collections.singletonList(new UserContextInterceptor()));
        }else{
            interceptores.add(new UserContextInterceptor());
            template.setInterceptors(interceptores);
        }

        return template;
    }

}
