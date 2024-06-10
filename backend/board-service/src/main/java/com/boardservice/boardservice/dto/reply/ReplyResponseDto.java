package com.boardservice.boardservice.dto.reply;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class ReplyResponseDto {
    private Integer replySeq;

    private String replyContent;

    private Integer boardSeq;

    private Integer userSeq;

    private String userId;
}
