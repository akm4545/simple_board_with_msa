package com.boardservice.boardservice.controller.client;

import com.boardservice.boardservice.dto.user.UserListRequestDto;
import com.boardservice.boardservice.dto.user.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("user-service")
public interface UserFeignClient {

    @ResponseBody
    @PostMapping("/user/list")
    ResponseEntity<List<UserResponseDto>> selectUserList(@RequestBody UserListRequestDto requestDto);

    @ResponseBody
    @GetMapping("/user/{userSeq}")
    ResponseEntity<UserResponseDto> selectUser(@PathVariable("userSeq") Integer userSeq);
}
