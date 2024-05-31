package com.simpleboard.userservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequestDto {
    private Integer userSeq;

    private String userId;

    private String userName;

    private String userPassword;
}
