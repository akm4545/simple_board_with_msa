package com.replyservice.replyservice.controller;

import com.replyservice.replyservice.dto.ReplyRequestDto;
import com.replyservice.replyservice.dto.ReplyResponseDto;
import com.replyservice.replyservice.dto.ReplySeqRequestDto;
import com.replyservice.replyservice.model.Reply;
import com.replyservice.replyservice.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @GetMapping("/reply/{replySeq}")
    public ResponseEntity<ReplyResponseDto> selectReply(@PathVariable Integer replySeq){
        ReplySeqRequestDto requestDto = ReplySeqRequestDto.builder()
                .replySeq(replySeq)
                .build();

        ReplyResponseDto responseDto = replyService.selectReply(requestDto);

        return ResponseEntity.ok(responseDto);

    }

    @GetMapping("/reply/list")
    public ResponseEntity<List<ReplyResponseDto>> selectReplyList(){
        List<ReplyResponseDto> responseDtoList = replyService.selectReplyList();

        return ResponseEntity.ok(responseDtoList);

    }

    @PostMapping("/reply")
    public ResponseEntity<ReplyResponseDto> insertReply(@RequestBody ReplyRequestDto requestDto){
        ReplyResponseDto responseDto = replyService.insertReplyList(requestDto);

        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/reply/{replySeq}")
    public ResponseEntity<ReplyResponseDto> updateReply(@PathVariable Integer replySeq,@RequestBody ReplyRequestDto requestDto){
        requestDto.setReplySeq(replySeq);

        ReplyResponseDto responseDto = replyService.updateReply(requestDto);

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/reply/{replySeq}")
    public void deleteReply(@PathVariable Integer replySeq){
        ReplySeqRequestDto requestDto = ReplySeqRequestDto.builder()
                .replySeq(replySeq)
                .build();

        replyService.deleteReply(requestDto);
    }
}
