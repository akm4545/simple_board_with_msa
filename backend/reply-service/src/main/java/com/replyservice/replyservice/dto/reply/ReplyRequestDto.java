package com.replyservice.replyservice.dto.reply;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class ReplyRequestDto {
    private Integer replySeq;

    private String replyContent;

    private Integer boardSeq;

    private Integer userSeq;
}
