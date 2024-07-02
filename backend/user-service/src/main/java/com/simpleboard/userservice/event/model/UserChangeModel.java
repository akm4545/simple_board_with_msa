package com.simpleboard.userservice.event.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserChangeModel {
    private String type;

    //이벤트를 발생시킨 액션
    private String action;

    //이벤트를 발생시킨 서비스 호출의 상관관계 ID
    private String correlationId;

    private Integer userSeq;

    private String userName;

    private String userId;

    private String userPassword;
}
