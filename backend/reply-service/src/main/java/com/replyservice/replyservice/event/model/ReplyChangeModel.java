package com.replyservice.replyservice.event.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class ReplyChangeModel {

    private Integer replySeq;

    private String replyContent;

    private Integer boardSeq;

    private Integer userSeq;

    private String type;

    //이벤트를 발생시킨 액션
    private String action;

    //이벤트를 발생시킨 서비스 호출의 상관관계 ID
    private String correlationId;

    private LocalDateTime publishDate;
}
