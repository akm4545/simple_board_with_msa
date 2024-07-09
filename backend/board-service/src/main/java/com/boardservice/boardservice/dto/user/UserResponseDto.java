package com.boardservice.boardservice.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Integer userSeq;

    private String userName;

    private String userId;
}
