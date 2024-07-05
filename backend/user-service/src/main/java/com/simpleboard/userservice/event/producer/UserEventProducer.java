package com.simpleboard.userservice.event.producer;

import com.simpleboard.userservice.dto.UserResponseDto;
import com.simpleboard.userservice.event.model.UserChangeModel;
import com.simpleboard.userservice.utils.ActionEnum;
import com.simpleboard.userservice.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.cloud.stream.function.StreamBridge;

@Component
@RequiredArgsConstructor
public class UserEventProducer {

    private String userPublish = "simpleBoard.user";

//    메세지 발행 공통 (채널명, 메세지)
    private final StreamBridge streamBridge;

    private static final Logger logger = LoggerFactory.getLogger(UserEventProducer.class);

    //Supplier는 일정한 간격으로 메시지를 전송하는 경우에 실행하는 로직을 구현하고,
    //Consumer는 메시지를 전달받았을 때 실행하는 로직을 구현합니다.
    //Function에는 메시지를 전달받았을 때 실행되는 로직과 리턴 값을 두도록 구현하여 output 바인딩을 통해 리턴 값을 메시지로 전송하는 경우에 사용됩니다.

    @Async
    public void send(ActionEnum action, UserResponseDto userDto) {
        logger.debug("Sending Kafka message {} for Organization Id: {}", action, userDto.getUserId());

        try{
            //    메세지 발행 공통 (채널명, 메세지)
            streamBridge.send(userPublish, UserChangeModel.builder()
                    .userId(userDto.getUserId())
                    .userSeq(userDto.getUserSeq())
                    .userPassword(userDto.getUserPassword())
                    .userName(userDto.getUserName())
                    .action(action.toString())
                    .correlationId(UserContext.getCorrelationId())
                    .type(UserChangeModel.class.getTypeName())
                    .build());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}