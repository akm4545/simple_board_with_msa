package com.boardservice.boardservice.service.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFeignClientProxy {

    private final UserFeignClient userFeignClient;
}
