package com.simpleboard.userservice.dto;

import com.simpleboard.userservice.model.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {
    private Integer userSeq;

    private String userName;

    private String userId;

    private String userPassword;

    public UserResponseDto(User user){
        this.userSeq = user.getUserSeq();
        this.userName = user.getUserName();
        this.userId = user.getUserId();
        this.userPassword = user.getUserPassword();
    }
}
