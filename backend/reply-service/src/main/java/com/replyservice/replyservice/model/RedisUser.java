package com.replyservice.replyservice.model;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@RedisHash
@Data
@Builder
public class RedisUser {
    private String type;

    //이벤트를 발생시킨 액션
    private String action;

    //이벤트를 발생시킨 서비스 호출의 상관관계 ID
    private String correlationId;

    @Id
    private String userSeq;

    private String userName;

    private String userId;

    private String userPassword;

    private LocalDateTime publishDate;
}
