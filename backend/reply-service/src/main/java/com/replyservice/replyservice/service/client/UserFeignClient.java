package com.replyservice.replyservice.service.client;

import com.replyservice.replyservice.dto.user.UserListRequestDto;
import com.replyservice.replyservice.dto.user.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@FeignClient("user-service")
public interface UserFeignClient {

    @ResponseBody
    @PostMapping("/user/list")
    ResponseEntity<List<UserResponseDto>> selectUserList(@RequestBody UserListRequestDto requestDto);
}
