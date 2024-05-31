package com.replyservice.replyservice.dto;

import com.replyservice.replyservice.model.Reply;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReplyResponseDto {
    private Integer replySeq;

    private String replyContent;

    public ReplyResponseDto(Reply reply){
        this.replySeq = reply.getReplySeq();
        this.replyContent = reply.getReplyContent();
    }
}
