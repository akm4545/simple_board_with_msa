package com.replyservice.replyservice.controller;

import com.replyservice.replyservice.model.Reply;
import com.replyservice.replyservice.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @GetMapping("/reply/{replySeq}")
    public Reply selectReply(@PathVariable Integer replySeq){
        return replyService.selectReply(replySeq);
    }

    @GetMapping("/reply/list")
    public List<Reply> selectReplyList(){
        return replyService.selectReplyList();
    }

    @PostMapping("/reply")
    public Reply insertReply(Reply reply){
        return replyService.insertReplyList(reply);
    }

    @PutMapping("/reply/{replySeq}")
    public Reply updateReply(@PathVariable Integer replySeq, Reply reply){
        reply.setReplySeq(replySeq);
        return replyService.updateReply(reply);
    }

    @DeleteMapping("/reply/{replySeq}")
    public void deleteReply(@PathVariable Integer replySeq){
        replyService.deleteReply(replySeq);
    }
}
