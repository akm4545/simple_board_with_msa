package com.simpleboard.userservice.event.consumer;

import com.simpleboard.userservice.dto.UserResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class UserMessageListener {

    Logger logger  = LoggerFactory.getLogger(UserMessageListener.class);

    @Bean
    public Consumer<UserResponseDto> useConsumer() {
        return userResponseDto -> {
            logger.warn("dididi");
            System.out.println("테스트 테스트 테스트 테스트" + userResponseDto.getUserSeq());
        };
    }
}
