package com.replyservice.replyservice.event.consumer;

import com.replyservice.replyservice.dto.reply.ReplyResponseDto;
import com.replyservice.replyservice.event.model.UserChangeModel;
import com.replyservice.replyservice.model.RedisUser;
import com.replyservice.replyservice.repository.RedisUserRepository;
import com.replyservice.replyservice.utils.ActionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class ReplyMessageListener {

    private final RedisUserRepository redisUserRepository;
    
//    1. 입력 -> 메시지 발급 -> 레디스 저장
//    2. 읽기 -> 레디스 조회 -> 없음 -> 직접 요청 -> 레디스 저장 -> 어떻게 처리할지 생각
//    3. 수정 -> 메시지 발급 -> 레디스 수정
//    4. 삭제 -> 메시지 발급 -> 레디스 삭제


    @Bean
    public Consumer<UserChangeModel> replyConsumer() {
        return userChangeModel -> {
            String userId = "user-" + userChangeModel.getUserSeq();
            Optional<RedisUser> redisUserOptional = redisUserRepository.findById(userId);

            if(redisUserOptional.isPresent()){
                RedisUser redisUser = redisUserOptional.get();

                if(userChangeModel.getType().equals(ActionEnum.DELETED.toString())){
                    redisUserRepository.deleteById(userId);
                }else{
                    if(redisUser.getPublishDate().isBefore(userChangeModel.getPublishDate())){
                        redisUserRepository.save(RedisUser.builder()
                                .type(userChangeModel.getType())
                                .action(userChangeModel.getAction())
                                .correlationId(userChangeModel.getCorrelationId())
                                .userSeq(userId)
                                .userId(userChangeModel.getUserId())
                                .publishDate(userChangeModel.getPublishDate())
                                .build());
                    }
                }
            }else{
                redisUserRepository.save(RedisUser.builder()
                        .type(userChangeModel.getType())
                        .action(userChangeModel.getAction())
                        .correlationId(userChangeModel.getCorrelationId())
                        .userSeq(userId)
                        .userId(userChangeModel.getUserId())
                        .publishDate(userChangeModel.getPublishDate())
                        .build());
            }
        };
    }
}
