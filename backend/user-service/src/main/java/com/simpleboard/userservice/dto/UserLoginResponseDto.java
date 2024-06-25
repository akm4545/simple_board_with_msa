package com.simpleboard.userservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginResponseDto {

    private String token;

    private String refreshToken;
}
