package com.replyservice.replyservice.service.client;

import com.replyservice.replyservice.dto.user.UserListRequestDto;
import com.replyservice.replyservice.dto.user.UserResponseDto;
import com.replyservice.replyservice.model.RedisUser;
import com.replyservice.replyservice.repository.RedisUserRepository;
import com.replyservice.replyservice.utils.ActionEnum;
import com.replyservice.replyservice.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
}
