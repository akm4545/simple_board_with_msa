package com.replyservice.replyservice.dto.reply;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReplyRequestDto {
    private Integer replySeq;

    private String replyContent;
}
