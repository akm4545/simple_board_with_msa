package com.boardservice.boardservice.event.consumer;

import com.boardservice.boardservice.dto.board.BoardResponseDto;
import com.boardservice.boardservice.dto.reply.ReplyResponseDto;
import com.boardservice.boardservice.dto.user.UserResponseDto;
import com.boardservice.boardservice.event.model.ReplyChangeModel;
import com.boardservice.boardservice.event.model.UserChangeModel;
import com.boardservice.boardservice.model.RedisReply;
import com.boardservice.boardservice.model.RedisUser;
import com.boardservice.boardservice.repository.RedisReplyRepository;
import com.boardservice.boardservice.repository.RedisUserRepository;
import com.boardservice.boardservice.utils.ActionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class BoardMessageListener {

    private final RedisUserRepository redisUserRepository;

    private final RedisReplyRepository redisReplyRepository;

    @Bean
    public Consumer<UserChangeModel> userConsumer() {
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

    @Bean
    public Consumer<ReplyChangeModel> replyConsumer() {
        return replyChangeModel -> {
            String replySeq = "reply-" + replyChangeModel.getReplySeq();
            Optional<RedisReply> redisReplyOptional = redisReplyRepository.findById(replySeq);

            if(redisReplyOptional.isPresent()){
                RedisReply redisReply = redisReplyOptional.get();

                if(replyChangeModel.getType().equals(ActionEnum.DELETED.toString())){
                    redisReplyRepository.deleteById(replySeq);
                }else{
                    if(redisReply.getPublishDate().isBefore(replyChangeModel.getPublishDate())){
                        redisReplyRepository.save(RedisReply.builder()
                                .type(replyChangeModel.getType())
                                .action(replyChangeModel.getAction())
                                .correlationId(replyChangeModel.getCorrelationId())
                                .userSeq(replyChangeModel.getUserSeq())
                                .replySeq(replySeq)
                                .replyContent(replyChangeModel.getReplyContent())
                                .boardSeq(replyChangeModel.getBoardSeq())
                                .publishDate(replyChangeModel.getPublishDate())
                                .build());
                    }
                }
            }else{
                redisReplyRepository.save(RedisReply.builder()
                        .type(replyChangeModel.getType())
                        .action(replyChangeModel.getAction())
                        .correlationId(replyChangeModel.getCorrelationId())
                        .userSeq(replyChangeModel.getUserSeq())
                        .replySeq(replySeq)
                        .replyContent(replyChangeModel.getReplyContent())
                        .boardSeq(replyChangeModel.getBoardSeq())
                        .publishDate(replyChangeModel.getPublishDate())
                        .build());
            }
        };
    }
}
