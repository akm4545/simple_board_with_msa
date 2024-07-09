package com.boardservice.boardservice.service.client;

import com.boardservice.boardservice.dto.reply.ReplyResponseDto;
import com.boardservice.boardservice.model.RedisReply;
import com.boardservice.boardservice.repository.RedisReplyRepository;
import com.boardservice.boardservice.utils.ActionEnum;
import com.boardservice.boardservice.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReplyFeignClientProxy {

    private final ReplyFeignClient replyFeignClient;

    private final RedisReplyRepository redisReplyRepository;

    public ResponseEntity<List<ReplyResponseDto>> selectReplyList(Integer boardSeq){
        List<RedisReply> redisReplyList = redisReplyRepository.findByBoardSeq(boardSeq);

        if(redisReplyList.size() == 0){
            ResponseEntity<List<ReplyResponseDto>> replyResponseListEntity = replyFeignClient.selectReplyList(boardSeq);
            redisReplyRepository.saveAll(replyResponseListEntity.getBody().stream()
                    .map(reply -> RedisReply.builder()
                            .type(ReplyResponseDto.class.getTypeName())
                            .action(ActionEnum.GET.toString())
                            .correlationId(UserContext.getCorrelationId())
                            .userSeq(reply.getUserSeq())
                            .replySeq("reply-" + reply.getReplySeq())
                            .replyContent(reply.getReplyContent())
                            .boardSeq(reply.getBoardSeq())
                            .publishDate(LocalDateTime.now())
                            .build())
                    .collect(Collectors.toList()));

            return replyResponseListEntity;
        }

        return ResponseEntity.ok(redisReplyList.stream()
                .map(reply -> ReplyResponseDto.builder()
                        .replySeq(Integer.parseInt(reply.getReplySeq().replaceAll("reply-", "")))
                        .replyContent(reply.getReplyContent())
                        .boardSeq(reply.getBoardSeq())
                        .userId(reply.getUserId())
                        .userSeq(reply.getUserSeq())
                        .build())
                .collect(Collectors.toList()));
    }
}
