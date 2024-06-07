package com.boardservice.boardservice.controller.client;

import com.boardservice.boardservice.dto.reply.ReplyResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("reply-service")
public interface ReplyFeignClient {

    @GetMapping("/board/{boardSeq}/reply/list")
    List<ReplyResponseDto> selectReplyList(@PathVariable Integer boardSeq);
}
