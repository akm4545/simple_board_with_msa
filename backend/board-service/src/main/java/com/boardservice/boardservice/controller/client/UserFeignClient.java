package com.boardservice.boardservice.controller.client;

import com.boardservice.boardservice.dto.user.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("user-service")
public interface UserFeignClient {

    @ResponseBody
    @PostMapping("/user/list")
    List<UserResponseDto> selectUserList(@RequestBody List<Integer> userSeqList);

    @ResponseBody
    @GetMapping("/user/{userSeq}")
    UserResponseDto selectUser(@PathVariable("userSeq") Integer userSeq);
}
