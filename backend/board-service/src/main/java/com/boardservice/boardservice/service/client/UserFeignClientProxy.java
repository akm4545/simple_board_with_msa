package com.boardservice.boardservice.service.client;

import com.boardservice.boardservice.dto.user.UserListRequestDto;
import com.boardservice.boardservice.dto.user.UserResponseDto;
import com.boardservice.boardservice.model.RedisUser;
import com.boardservice.boardservice.repository.RedisUserRepository;
import com.boardservice.boardservice.utils.ActionEnum;
import com.boardservice.boardservice.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserFeignClientProxy {

    private final UserFeignClient userFeignClient;

    private final RedisUserRepository redisUserRepository;

    public ResponseEntity<List<UserResponseDto>> selectUserList(UserListRequestDto requestDto){
        List<Integer> userSeqList = requestDto.getUserSeqList();
        List<String> userSearchList = new ArrayList<String>();

        for(int i=0; i<userSeqList.size(); i++){
            String redisUserId = "user-" + userSeqList.get(i);
            userSearchList.add(redisUserId);
        }

        List<RedisUser> redisUserList = (List<RedisUser>) redisUserRepository.findAllById(userSearchList);
        List<Integer> redisUserSeqList = new ArrayList<Integer>();
        List<UserResponseDto> userResponseDtoList = new ArrayList<UserResponseDto>();

        redisUserList.forEach(user -> {
            userResponseDtoList.add(UserResponseDto.builder()
                    .userSeq(Integer.parseInt(user.getUserSeq().replace("user-", "")))
                    .userName(user.getUserName())
                    .userId(user.getUserId())
                    .build());

            redisUserSeqList.add(Integer.parseInt(user.getUserSeq().replace("user-", "")));
        });

        if(redisUserList.size() != userSeqList.size()){
            requestDto.getUserSeqList().removeAll(redisUserSeqList);
            ResponseEntity<List<UserResponseDto>> userResponse = userFeignClient.selectUserList(requestDto);

            redisUserRepository.saveAll(userResponse.getBody().stream()
                    .map(user -> RedisUser.builder()
                            .type(UserResponseDto.class.getTypeName())
                            .action(ActionEnum.GET.toString())
                            .correlationId(UserContext.getCorrelationId())
                            .userSeq("user-" + user.getUserSeq())
                            .userId(user.getUserId())
                            .publishDate(LocalDateTime.now())
                            .build())
                    .collect(Collectors.toList()));

            userResponseDtoList.addAll(userResponse.getBody());
        }

        return ResponseEntity.ok(userResponseDtoList);
    };

    public ResponseEntity<UserResponseDto> selectUser(Integer userSeq) {
        String redisUserSeq = "user-" + userSeq;
        Optional<RedisUser> optionalRedisUser = redisUserRepository.findById(redisUserSeq);

        if(optionalRedisUser.isPresent()) {
            RedisUser redisUser = optionalRedisUser.get();

            return ResponseEntity.ok(UserResponseDto.builder()
                            .userSeq(userSeq)
                            .userName(redisUser.getUserName())
                            .userId(redisUser.getUserId())
                    .build());
        }

        ResponseEntity<UserResponseDto> userResponseDtoResponseEntity = userFeignClient.selectUser(userSeq);
        UserResponseDto userResponseDto = userResponseDtoResponseEntity.getBody();
        redisUserRepository.save(RedisUser.builder()
                .type(UserResponseDto.class.getTypeName())
                .action(ActionEnum.GET.toString())
                .correlationId(UserContext.getCorrelationId())
                .userSeq("user-" + userResponseDto.getUserSeq())
                .userId(userResponseDto.getUserId())
                .publishDate(LocalDateTime.now())
                .build());

        return userFeignClient.selectUser(userSeq);
    };
}
