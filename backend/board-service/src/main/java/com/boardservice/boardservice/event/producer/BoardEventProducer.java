package com.boardservice.boardservice.event.producer;

import com.boardservice.boardservice.dto.board.BoardResponseDto;
import com.boardservice.boardservice.event.model.BoardChangeModel;
import com.boardservice.boardservice.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoardEventProducer {

    @Value("test")
    private String userPublish = "test";

    //    메세지 발행 공통 (채널명, 메세지)
    private final StreamBridge streamBridge;

    private static final Logger logger = LoggerFactory.getLogger(BoardEventProducer.class);

    //Supplier는 일정한 간격으로 메시지를 전송하는 경우에 실행하는 로직을 구현하고,
    //Consumer는 메시지를 전달받았을 때 실행하는 로직을 구현합니다.
    //Function에는 메시지를 전달받았을 때 실행되는 로직과 리턴 값을 두도록 구현하여 output 바인딩을 통해 리턴 값을 메시지로 전송하는 경우에 사용됩니다.

    public void send(String action, BoardResponseDto boardDto) {
        logger.debug("Sending Kafka message {} for Organization Id: {}", action, boardDto.getBoardSeq());

        //    메세지 발행 공통 (채널명, 메세지)
        streamBridge.send(userPublish, BoardChangeModel.builder()
                        .boardTitle(boardDto.getBoardTitle())
                        .userSeq(boardDto.getUserSeq())
                        .boardContent(boardDto.getBoardContent())
                        .action(action)
                        .correlationId(UserContext.getCorrelationId())
                .build());
    }
}
