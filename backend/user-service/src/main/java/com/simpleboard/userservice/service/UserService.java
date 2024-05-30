package com.simpleboard.userservice.service;

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
    public User selectUser(Integer userSeq) {
        return userRepository.findById(userSeq).get();
    }

    @Transactional
    public User insertUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(User user) {
        User updateUser = userRepository.findById(user.getUserSeq()).get();

        updateUser.setUserName(user.getUserName());
        updateUser.setUserPassword(user.getUserPassword());
        updateUser.setUserName(user.getUserName());

        return updateUser;
    }

    @Transactional
    public void deleteUser(Integer userSeq) {
        userRepository.deleteById(userSeq);
    }
}
