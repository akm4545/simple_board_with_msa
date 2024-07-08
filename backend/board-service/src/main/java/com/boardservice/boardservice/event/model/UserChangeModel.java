package com.boardservice.boardservice.event.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserChangeModel {
    private String type;

    //이벤트를 발생시킨 액션
    private String action;

    //이벤트를 발생시킨 서비스 호출의 상관관계 ID
    private String correlationId;

    private Integer userSeq;

    private String userId;

    private LocalDateTime publishDate;
}
