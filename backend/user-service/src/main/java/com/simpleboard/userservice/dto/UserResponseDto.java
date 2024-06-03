package com.simpleboard.userservice.dto;

import com.simpleboard.userservice.model.User;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
//HATEOAS 구현을 위해 상속
public class UserResponseDto extends RepresentationModel<UserResponseDto> {
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
