package com.replyservice.replyservice.controller;

import com.replyservice.replyservice.dto.reply.ReplyRequestDto;
import com.replyservice.replyservice.dto.reply.ReplyResponseDto;
import com.replyservice.replyservice.dto.reply.ReplySeqRequestDto;
import com.replyservice.replyservice.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//HATEOAS 사용을 위해 import
import javax.annotation.security.RolesAllowed;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @GetMapping("/board/{boardSeq}/reply/list")
    public ResponseEntity<List<ReplyResponseDto>> selectReplyList(@PathVariable Integer boardSeq){
        List<ReplyResponseDto> responseDtoList = replyService.selectReplyList(boardSeq);

        responseDtoList.forEach(responseDto -> responseDto.add(
                linkTo(methodOn(ReplyController.class)
                        .selectReplyList(boardSeq))
                        .withSelfRel(),
                linkTo(methodOn(ReplyController.class)
                        .insertReply(ReplyRequestDto.builder().build()))
                        .withRel("insertReply"),
                linkTo(methodOn(ReplyController.class)
                        .updateReply(responseDto.getReplySeq(), ReplyRequestDto.builder().build()))
                        .withRel("updateReply"),
                linkTo(methodOn(ReplyController.class)
                        .deleteReply(responseDto.getReplySeq()))
                        .withRel("deleteReply")));

        return ResponseEntity.ok(responseDtoList);

    }

    @PostMapping("/reply")
    public ResponseEntity<ReplyResponseDto> insertReply(@RequestBody ReplyRequestDto requestDto){
        ReplyResponseDto responseDto = replyService.insertReplyList(requestDto);

        responseDto.add(linkTo(methodOn(ReplyController.class)
                        .selectReplyList(0))
                        .withRel("selectReplyList"),
                linkTo(methodOn(ReplyController.class)
                        .insertReply(ReplyRequestDto.builder().build()))
                        .withSelfRel(),
                linkTo(methodOn(ReplyController.class)
                        .updateReply(requestDto.getReplySeq(), ReplyRequestDto.builder().build()))
                        .withRel("updateReply"),
                linkTo(methodOn(ReplyController.class)
                        .deleteReply(requestDto.getReplySeq()))
                        .withRel("deleteReply"));

        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/reply/{replySeq}")
    public ResponseEntity<ReplyResponseDto> updateReply(@PathVariable Integer replySeq,@RequestBody ReplyRequestDto requestDto){
        requestDto.setReplySeq(replySeq);

        ReplyResponseDto responseDto = replyService.updateReply(requestDto);

        responseDto.add(linkTo(methodOn(ReplyController.class)
                        .selectReplyList(0))
                        .withRel("selectReplyList"),
                linkTo(methodOn(ReplyController.class)
                        .insertReply(ReplyRequestDto.builder().build()))
                        .withRel("insertReply"),
                linkTo(methodOn(ReplyController.class)
                        .updateReply(replySeq, ReplyRequestDto.builder().build()))
                        .withSelfRel(),
                linkTo(methodOn(ReplyController.class)
                        .deleteReply(replySeq))
                        .withRel("deleteReply"));

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/reply/{replySeq}")
    public ResponseEntity<ReplyResponseDto> deleteReply(@PathVariable Integer replySeq){
        ReplySeqRequestDto requestDto = ReplySeqRequestDto.builder()
                .replySeq(replySeq)
                .build();

        replyService.deleteReply(requestDto);

        ReplyResponseDto responseDto = ReplyResponseDto.builder().build();

        responseDto.add(linkTo(methodOn(ReplyController.class)
                        .selectReplyList(0))
                        .withRel("selectReplyList"),
                linkTo(methodOn(ReplyController.class)
                        .insertReply(ReplyRequestDto.builder().build()))
                        .withRel("insertReply"),
                linkTo(methodOn(ReplyController.class)
                        .updateReply(replySeq, ReplyRequestDto.builder().build()))
                        .withRel("updateReply"),
                linkTo(methodOn(ReplyController.class)
                        .deleteReply(replySeq))
                        .withSelfRel());

        return ResponseEntity.ok(responseDto);
    }
}
