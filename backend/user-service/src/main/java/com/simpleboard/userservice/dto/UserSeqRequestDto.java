package com.simpleboard.userservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSeqRequestDto {
    private Integer userSeq;
}
