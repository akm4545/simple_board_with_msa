package com.replyservice.replyservice.event.consumer;

import com.replyservice.replyservice.dto.reply.ReplyResponseDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class ReplyMessageListener {

    @Bean
    public Consumer<ReplyResponseDto> replyConsumer() {
        return replyResponseDto -> {
            System.out.println("테스트 테스트 테스트 테스트" + replyResponseDto.getUserSeq());
        };
    }
}
