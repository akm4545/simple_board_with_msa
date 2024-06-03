package com.simpleboard.userservice.controller;

import com.simpleboard.userservice.dto.UserRequestDto;
import com.simpleboard.userservice.dto.UserSeqRequestDto;
import com.simpleboard.userservice.dto.UserResponseDto;
import com.simpleboard.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//HATEOAS 사용을 위해 import
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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

    @PostMapping("/user")
    public ResponseEntity<UserResponseDto> insertUser(@RequestBody UserRequestDto requestDto) {
        UserResponseDto responseDto = userService.insertUser(requestDto);
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
                        .withSelfRel(),
                linkTo(methodOn(UserController.class)
                        .deleteUser(requestDto.getUserSeq()))
                        .withSelfRel());

        return ResponseEntity.ok(responseDto);
    }
}
