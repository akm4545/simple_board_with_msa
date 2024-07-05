package com.replyservice.replyservice.event.producer;

import com.replyservice.replyservice.dto.reply.ReplyResponseDto;
import com.replyservice.replyservice.event.model.ReplyChangeModel;
import com.replyservice.replyservice.utils.ActionEnum;
import com.replyservice.replyservice.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReplyEventProducer {
    private String replyPublish = "simpleBoard.reply";

    //    메세지 발행 공통 (채널명, 메세지)
    private final StreamBridge streamBridge;

    private static final Logger logger = LoggerFactory.getLogger(ReplyEventProducer.class);

    //Supplier는 일정한 간격으로 메시지를 전송하는 경우에 실행하는 로직을 구현하고,
    //Consumer는 메시지를 전달받았을 때 실행하는 로직을 구현합니다.
    //Function에는 메시지를 전달받았을 때 실행되는 로직과 리턴 값을 두도록 구현하여 output 바인딩을 통해 리턴 값을 메시지로 전송하는 경우에 사용됩니다.

    @Async
    public void send(ActionEnum action, ReplyResponseDto replyDto) {
        logger.debug("Sending Kafka message {} for Organization Id: {}", action, replyDto.getReplySeq());

        //    메세지 발행 공통 (채널명, 메세지)
        streamBridge.send(replyPublish, ReplyChangeModel.builder()
                .replySeq(replyDto.getReplySeq())
                .boardSeq(replyDto.getBoardSeq())
                .replyContent(replyDto.getReplyContent())
                .userSeq(replyDto.getUserSeq())
                .action(action.toString())
                .correlationId(UserContext.getCorrelationId())
                .type(ReplyChangeModel.class.getTypeName())
                .build());
    }
}
