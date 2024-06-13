package com.simpleboard.userservice.service;

import com.simpleboard.userservice.dto.UserListRequestDto;
import com.simpleboard.userservice.dto.UserRequestDto;
import com.simpleboard.userservice.dto.UserSeqRequestDto;
import com.simpleboard.userservice.dto.UserResponseDto;
import com.simpleboard.userservice.model.User;
import com.simpleboard.userservice.repository.UserRepository;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    @CircuitBreaker(name = "userService", fallbackMethod = "buildFallbackUser")
    @Bulkhead(name="bulkheadUserService", fallbackMethod = "buildFallbackUser")
    @Retry(name = "retryUserService", fallbackMethod = "buildFallbackUser")
    public UserResponseDto selectUser(UserSeqRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserSeq()).get();

        return new UserResponseDto(user);
    }

    @Transactional
    @CircuitBreaker(name = "userService", fallbackMethod = "buildFallbackUser")
    @Bulkhead(name="bulkheadUserService", fallbackMethod = "buildFallbackUser")
    @Retry(name = "retryUserService", fallbackMethod = "buildFallbackUser")
    public UserResponseDto insertUser(UserRequestDto requestDto) {
        User user = new User();

        user.setUserId(requestDto.getUserId());
        user.setUserName(requestDto.getUserName());
        user.setUserPassword(requestDto.getUserPassword());

        UserResponseDto responseDto = new UserResponseDto(userRepository.save(user));

        return responseDto;
    }

    @Transactional
    @CircuitBreaker(name = "userService", fallbackMethod = "buildFallbackUser")
    @Bulkhead(name="bulkheadUserService", fallbackMethod = "buildFallbackUser")
    @Retry(name = "retryUserService", fallbackMethod = "buildFallbackUser")
    public UserResponseDto updateUser(UserRequestDto requestDto) {
        User updateUser = userRepository.findById(requestDto.getUserSeq()).get();

        updateUser.setUserName(requestDto.getUserName());
        updateUser.setUserPassword(requestDto.getUserPassword());
        updateUser.setUserName(requestDto.getUserName());

        UserResponseDto responseDto = new UserResponseDto(updateUser);

        return responseDto;
    }

    @Transactional
    @CircuitBreaker(name = "userService")
    @Bulkhead(name="bulkheadUserService")
    @Retry(name = "retryUserService")
    public void deleteUser(UserSeqRequestDto requestDto) {
        userRepository.deleteById(requestDto.getUserSeq());
    }

    public List<UserResponseDto> selectUserList(UserListRequestDto requestDto) {
        List<User> userList = userRepository.findAllById(requestDto.getUserSeqList());
        List<UserResponseDto> responseDtoList = userList.stream().map(UserResponseDto::new).toList();

        return responseDtoList;
    }

    private ResponseEntity<UserResponseDto> buildFallbackUser() {
        return ResponseEntity.ok(new UserResponseDto());
    }

    private ResponseEntity<List<UserResponseDto>> buildFallbackUserList() {
        return ResponseEntity.ok(List.of(new UserResponseDto()));
    }
}
