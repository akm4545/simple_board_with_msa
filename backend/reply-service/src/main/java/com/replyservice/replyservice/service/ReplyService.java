package com.replyservice.replyservice.service;

import com.replyservice.replyservice.dto.reply.ReplyRequestDto;
import com.replyservice.replyservice.dto.reply.ReplyResponseDto;
import com.replyservice.replyservice.dto.reply.ReplySeqRequestDto;
import com.replyservice.replyservice.dto.user.UserListRequestDto;
import com.replyservice.replyservice.dto.user.UserResponseDto;
import com.replyservice.replyservice.model.Reply;
import com.replyservice.replyservice.repository.ReplyRepository;
import com.replyservice.replyservice.service.client.UserFeignClient;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReplyService {

    private final ReplyRepository replyRepository;

    private final UserFeignClient userFeignClient;

    @CircuitBreaker(name = "replyService", fallbackMethod = "buildFallbackReplyList")
    @Bulkhead(name="bulkheadReplyService", fallbackMethod = "buildFallbackReplyList")
    public List<ReplyResponseDto> selectReplyList(Integer boardSeq) {
        List<Reply> replyList = replyRepository.findByBoardSeq(boardSeq);
        List<Integer> userSeqList = replyList.stream().map(Reply::getUserSeq).toList();
        UserListRequestDto userRequestDto = UserListRequestDto.builder()
                .userSeqList(userSeqList)
                .build();

        ResponseEntity<List<UserResponseDto>> userListResponse = userFeignClient.selectUserList(userRequestDto);
        Map<Integer, UserResponseDto> userMap = userListResponse.getBody().stream().collect(Collectors.toMap(
                UserResponseDto::getUserSeq,
                value -> value
        ));
        List<ReplyResponseDto> responseDtoList = replyList.stream().map(reply -> {
            return new ReplyResponseDto(reply, userMap.get(reply.getUserSeq()));
        }).toList();

        return responseDtoList;
    }

    @Transactional
    @CircuitBreaker(name = "replyService", fallbackMethod = "buildFallbackReply")
    @Bulkhead(name="bulkheadReplyService", fallbackMethod = "buildFallbackReply")
    public ReplyResponseDto insertReplyList(ReplyRequestDto requestDto) {
        Reply reply = new Reply();
        reply.setReplyContent(requestDto.getReplyContent());
        reply.setBoardSeq(requestDto.getBoardSeq());
        reply.setUserSeq(requestDto.getUserSeq());

        ReplyResponseDto responseDto = new ReplyResponseDto(replyRepository.save(reply));

        return responseDto;
    }

    @Transactional
    @CircuitBreaker(name = "replyService", fallbackMethod = "buildFallbackReply")
    @Bulkhead(name="bulkheadReplyService", fallbackMethod = "buildFallbackReply")
    public ReplyResponseDto updateReply(ReplyRequestDto requestDto) {
        Reply updateReply = replyRepository.findById(requestDto.getReplySeq()).get();
        updateReply.setReplyContent(requestDto.getReplyContent());

        ReplyResponseDto responseDto = new ReplyResponseDto(updateReply);

        return responseDto;
    }

    @Transactional
    @CircuitBreaker(name = "replyService")
    @Bulkhead(name="bulkheadReplyService")
    public void deleteReply(ReplySeqRequestDto requestDto) {
        replyRepository.deleteById(requestDto.getReplySeq());
    }

    private ResponseEntity<ReplyRequestDto> buildFallbackReply() {
        return ResponseEntity.ok(new ReplyRequestDto());
    }

    private ResponseEntity<List<ReplyResponseDto>> buildFallbackReplyList() {
        return ResponseEntity.ok(List.of(new ReplyResponseDto()));
    }
}
