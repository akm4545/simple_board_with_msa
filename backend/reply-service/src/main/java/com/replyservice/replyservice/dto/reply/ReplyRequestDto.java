package com.replyservice.replyservice.dto.reply;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyRequestDto {
    private Integer replySeq;

    private String replyContent;

    private Integer boardSeq;

    private Integer userSeq;
}
