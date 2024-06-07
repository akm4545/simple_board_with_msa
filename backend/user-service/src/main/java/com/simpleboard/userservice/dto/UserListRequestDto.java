package com.simpleboard.userservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserListRequestDto {
    private List<Integer> userSeqList;
}
