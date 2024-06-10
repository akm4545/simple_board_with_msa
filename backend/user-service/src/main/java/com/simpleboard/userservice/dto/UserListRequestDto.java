package com.simpleboard.userservice.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListRequestDto {
    private List<Integer> userSeqList;
}
