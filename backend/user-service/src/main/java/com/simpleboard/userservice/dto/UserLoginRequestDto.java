package com.simpleboard.userservice.dto;

import lombok.Data;

@Data
public class UserLoginRequestDto {
    private String id;

    private String password;
}
