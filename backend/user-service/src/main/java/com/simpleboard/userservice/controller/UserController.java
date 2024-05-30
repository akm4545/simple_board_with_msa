package com.simpleboard.userservice.controller;

import com.simpleboard.userservice.model.User;
import com.simpleboard.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user/{userSeq}")
    public User selectUser(@PathVariable Integer userSeq) {
        return userService.selectUser(userSeq);
    }

    @PostMapping("/user")
    public User insertUser(User user) {
        return userService.insertUser(user);
    }

    @PutMapping("/user/{userSeq}")
    public User updateUser(@PathVariable Integer userSeq, User user){
        user.setUserSeq(userSeq);

        return userService.updateUser(user);
    }

    @DeleteMapping("/user/{userSeq}")
    public void deleteUser(@PathVariable Integer userSeq){
        userService.deleteUser(userSeq);
    }
}
