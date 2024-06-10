package com.replyservice.replyservice.dto.reply;

import com.replyservice.replyservice.dto.user.UserResponseDto;
import com.replyservice.replyservice.model.Reply;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
//HATEOAS 구현을 위해 상속
public class ReplyResponseDto extends RepresentationModel<ReplyResponseDto> {
    private Integer replySeq;

    private String replyContent;

    private Integer boardSeq;

    private Integer userSeq;

    private String userId;

    public ReplyResponseDto(Reply reply){
        this.replySeq = reply.getReplySeq();
        this.replyContent = reply.getReplyContent();
        this.boardSeq = reply.getBoardSeq();
        this.userSeq = reply.getUserSeq();
    }

    public ReplyResponseDto(Reply reply, UserResponseDto userResponseDto){
        this.replySeq = reply.getReplySeq();
        this.replyContent = reply.getReplyContent();
        this.boardSeq = reply.getBoardSeq();
        this.userSeq = reply.getUserSeq();
        this.userId = userResponseDto.getUserId();
    }
}
