package com.simpleboard.userservice.controller;

import com.simpleboard.userservice.dto.*;
import com.simpleboard.userservice.service.AuthService;
import com.simpleboard.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//HATEOAS 사용을 위해 import
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final AuthService authService;

    @GetMapping("/user/{userSeq}")
    public ResponseEntity<UserResponseDto> selectUser(@PathVariable Integer userSeq) {
        UserSeqRequestDto requestDto = UserSeqRequestDto.builder()
                .userSeq(userSeq)
                .build();

//        HATEOAS 설정
        UserResponseDto responseDto = userService.selectUser(requestDto);
        responseDto.add(linkTo(methodOn(UserController.class)
                .selectUser(userSeq))
                .withSelfRel(),
                linkTo(methodOn(UserController.class)
                        .insertUser(UserRequestDto.builder().build()))
                        .withRel("insertUser"),
                linkTo(methodOn(UserController.class)
                        .updateUser(userSeq, UserRequestDto.builder().build()))
                        .withRel("updateUser"),
                linkTo(methodOn(UserController.class)
                        .deleteUser(userSeq))
                        .withRel("deleteUser"));

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/user/list")
    public ResponseEntity<List<UserResponseDto>> selectUserList(@RequestBody UserListRequestDto requestDto) {
        List<UserResponseDto> responseDtoList = userService.selectUserList(requestDto);

        return ResponseEntity.ok(responseDtoList);
    }

    @PostMapping("/user")
    public ResponseEntity<UserResponseDto> insertUser(@RequestBody UserRequestDto requestDto) {
        UserResponseDto responseDto = userService.insertUser(requestDto);
        authService.insertUser(requestDto);
        responseDto.add(linkTo(methodOn(UserController.class)
                        .selectUser(requestDto.getUserSeq()))
                        .withRel("selectUser"),
                linkTo(methodOn(UserController.class)
                        .insertUser(UserRequestDto.builder().build()))
                        .withSelfRel(),
                linkTo(methodOn(UserController.class)
                        .updateUser(requestDto.getUserSeq(), UserRequestDto.builder().build()))
                        .withRel("updateUser"),
                linkTo(methodOn(UserController.class)
                        .deleteUser(requestDto.getUserSeq()))
                        .withRel("deleteUser"));

        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/user/{userSeq}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Integer userSeq, @RequestBody UserRequestDto requestDto){
        requestDto.setUserSeq(userSeq);
        UserResponseDto responseDto = userService.updateUser(requestDto);

        responseDto.add(linkTo(methodOn(UserController.class)
                        .selectUser(requestDto.getUserSeq()))
                        .withRel("selectUser"),
                linkTo(methodOn(UserController.class)
                        .insertUser(UserRequestDto.builder().build()))
                        .withRel("insertUser"),
                linkTo(methodOn(UserController.class)
                        .updateUser(requestDto.getUserSeq(), UserRequestDto.builder().build()))
                        .withSelfRel(),
                linkTo(methodOn(UserController.class)
                        .deleteUser(requestDto.getUserSeq()))
                        .withRel("deleteUser"));

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/user/{userSeq}")
    public ResponseEntity<UserResponseDto> deleteUser(@PathVariable Integer userSeq){
        UserSeqRequestDto requestDto = UserSeqRequestDto.builder()
                .userSeq(userSeq)
                .build();

        userService.deleteUser(requestDto);

        UserResponseDto responseDto = UserResponseDto.builder().build();

        responseDto.add(linkTo(methodOn(UserController.class)
                        .selectUser(requestDto.getUserSeq()))
                        .withRel("selectUser"),
                linkTo(methodOn(UserController.class)
                        .insertUser(UserRequestDto.builder().build()))
                        .withRel("insertUser"),
                linkTo(methodOn(UserController.class)
                        .updateUser(requestDto.getUserSeq(), UserRequestDto.builder().build()))
                        .withRel("updateUser"),
                linkTo(methodOn(UserController.class)
                        .deleteUser(requestDto.getUserSeq()))
                        .withSelfRel());

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> loginUser(@RequestBody UserLoginRequestDto requestDto) {
        AccessTokenResponse tokenResponse = authService.setAuth(requestDto); // Token 발급

        return ResponseEntity.ok(UserLoginResponseDto.builder()
                .token(tokenResponse.getToken())
                .refreshToken(tokenResponse.getRefreshToken())
                .build());
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<UserLoginResponseDto> refreshToken(@RequestBody RefreshTokenRequestDto requestDto) {
        return ResponseEntity.ok(authService.refreshToken(requestDto));
    }
}
