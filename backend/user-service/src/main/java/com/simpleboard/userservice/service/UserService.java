package com.simpleboard.userservice.service;

import com.simpleboard.userservice.dto.UserListRequestDto;
import com.simpleboard.userservice.dto.UserRequestDto;
import com.simpleboard.userservice.dto.UserSeqRequestDto;
import com.simpleboard.userservice.dto.UserResponseDto;
import com.simpleboard.userservice.event.producer.UserEventProducer;
import com.simpleboard.userservice.model.User;
import com.simpleboard.userservice.repository.UserRepository;
import com.simpleboard.userservice.utils.ActionEnum;
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

    private final UserEventProducer userEventProducer;

    @CircuitBreaker(name = "userService", fallbackMethod = "buildFallbackUser")
    @Bulkhead(name="bulkheadUserService", fallbackMethod = "buildFallbackUser")
    @Retry(name = "retryUserService", fallbackMethod = "buildFallbackUser")
    public UserResponseDto selectUser(UserSeqRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserSeq()).get();
        UserResponseDto userResponseDto = new UserResponseDto(user);

        userEventProducer.send(ActionEnum.GET, userResponseDto);

        return userResponseDto;
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

        userEventProducer.send(ActionEnum.CREATED, responseDto);

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

        userEventProducer.send(ActionEnum.UPDATED, responseDto);

        return responseDto;
    }

    @Transactional
    @CircuitBreaker(name = "userService")
    @Bulkhead(name="bulkheadUserService")
    @Retry(name = "retryUserService")
    public void deleteUser(UserSeqRequestDto requestDto) {

        userRepository.deleteById(requestDto.getUserSeq());

        userEventProducer.send(ActionEnum.DELETED, UserResponseDto.builder()
                        .userSeq(requestDto.getUserSeq())
                .build());
    }

    public List<UserResponseDto> selectUserList(UserListRequestDto requestDto) {
        List<User> userList = userRepository.findAllById(requestDto.getUserSeqList());
        List<UserResponseDto> responseDtoList = userList.stream().map(UserResponseDto::new).toList();

        return responseDtoList;
    }

    private UserResponseDto buildFallbackUser(UserSeqRequestDto requestDto, Throwable t) {
        return new UserResponseDto();
    }

    private UserResponseDto buildFallbackUser(UserRequestDto requestDto, Throwable t) {
        return new UserResponseDto();
    }
}
