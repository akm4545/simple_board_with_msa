package com.simpleboard.userservice.controller;

import com.simpleboard.userservice.dto.UserRequestDto;
import com.simpleboard.userservice.dto.UserSeqRequestDto;
import com.simpleboard.userservice.dto.UserResponseDto;
import com.simpleboard.userservice.model.User;
import com.simpleboard.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user/{userSeq}")
    public ResponseEntity<UserResponseDto> selectUser(@PathVariable Integer userSeq) {
        UserSeqRequestDto requestDto = UserSeqRequestDto.builder()
                .userSeq(userSeq)
                .build();

        return ResponseEntity.ok(userService.selectUser(requestDto));
    }

    @PostMapping("/user")
    public ResponseEntity<UserResponseDto> insertUser(@RequestBody UserRequestDto requestDto) {
        UserResponseDto responseDto = userService.insertUser(requestDto);

        return ResponseEntity.ok(responseDto);

    }

    @PutMapping("/user/{userSeq}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Integer userSeq, @RequestBody UserRequestDto requestDto){
        requestDto.setUserSeq(userSeq);
        UserResponseDto responseDto = userService.updateUser(requestDto);

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/user/{userSeq}")
    public void deleteUser(@PathVariable Integer userSeq){
        UserSeqRequestDto requestDto = UserSeqRequestDto.builder()
                .userSeq(userSeq)
                .build();

        userService.deleteUser(requestDto);
    }
}
