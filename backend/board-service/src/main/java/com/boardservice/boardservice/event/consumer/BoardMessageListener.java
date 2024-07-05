package com.boardservice.boardservice.event.consumer;

import com.boardservice.boardservice.dto.board.BoardResponseDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class BoardMessageListener {

    @Bean
    public Consumer<BoardResponseDto> boardConsumer() {
        return boardResponseDto -> {
            System.out.println("테스트 테스트 테스트 테스트" + boardResponseDto.getUserSeq());
        };
    }
}
