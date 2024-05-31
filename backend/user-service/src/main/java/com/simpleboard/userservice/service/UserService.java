package com.simpleboard.userservice.service;

import com.simpleboard.userservice.dto.UserRequestDto;
import com.simpleboard.userservice.dto.UserSeqRequestDto;
import com.simpleboard.userservice.dto.UserResponseDto;
import com.simpleboard.userservice.model.User;
import com.simpleboard.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    public UserResponseDto selectUser(UserSeqRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserSeq()).get();

        return new UserResponseDto(user);
    }

    @Transactional
    public UserResponseDto insertUser(UserRequestDto requestDto) {
        User user = new User();

        user.setUserId(requestDto.getUserId());
        user.setUserName(requestDto.getUserName());
        user.setUserPassword(requestDto.getUserPassword());

        UserResponseDto responseDto = new UserResponseDto(userRepository.save(user));

        return responseDto;
    }

    @Transactional
    public UserResponseDto updateUser(UserRequestDto requestDto) {
        User updateUser = userRepository.findById(requestDto.getUserSeq()).get();

        updateUser.setUserName(requestDto.getUserName());
        updateUser.setUserPassword(requestDto.getUserPassword());
        updateUser.setUserName(requestDto.getUserName());

        UserResponseDto responseDto = new UserResponseDto(updateUser);

        return responseDto;
    }

    @Transactional
    public void deleteUser(UserSeqRequestDto requestDto) {
        userRepository.deleteById(requestDto.getUserSeq());
    }
}
