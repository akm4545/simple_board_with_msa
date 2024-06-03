package com.replyservice.replyservice.dto;

import com.replyservice.replyservice.model.Reply;
import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
//HATEOAS 구현을 위해 상속
public class ReplyResponseDto extends RepresentationModel<ReplyResponseDto> {
    private Integer replySeq;

    private String replyContent;

    public ReplyResponseDto(Reply reply){
        this.replySeq = reply.getReplySeq();
        this.replyContent = reply.getReplyContent();
    }
}
