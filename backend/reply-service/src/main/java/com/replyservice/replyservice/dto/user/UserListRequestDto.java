package com.replyservice.replyservice.dto.user;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserListRequestDto {
    private List<Integer> userSeqList;
}
