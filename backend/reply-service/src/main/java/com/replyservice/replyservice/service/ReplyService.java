package com.replyservice.replyservice.service;

import com.replyservice.replyservice.dto.ReplyRequestDto;
import com.replyservice.replyservice.dto.ReplyResponseDto;
import com.replyservice.replyservice.dto.ReplySeqRequestDto;
import com.replyservice.replyservice.model.Reply;
import com.replyservice.replyservice.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReplyService {

    private final ReplyRepository replyRepository;

    public ReplyResponseDto selectReply(ReplySeqRequestDto requestDto) {
        Reply reply = replyRepository.findById(requestDto.getReplySeq()).get();
        ReplyResponseDto responseDto = new ReplyResponseDto(reply);

        return responseDto;
    }

    public List<ReplyResponseDto> selectReplyList() {
        List<Reply> replyList = replyRepository.findAll();
        List<ReplyResponseDto> responseDtoList = replyList.stream().map(ReplyResponseDto::new).toList();

        return responseDtoList;
    }

    @Transactional
    public ReplyResponseDto insertReplyList(ReplyRequestDto requestDto) {
        Reply reply = new Reply();
        reply.setReplyContent(requestDto.getReplyContent());

        ReplyResponseDto responseDto = new ReplyResponseDto(replyRepository.save(reply));

        return responseDto;
    }

    @Transactional
    public ReplyResponseDto updateReply(ReplyRequestDto requestDto) {
        Reply updateReply = replyRepository.findById(requestDto.getReplySeq()).get();
        updateReply.setReplyContent(requestDto.getReplyContent());

        ReplyResponseDto responseDto = new ReplyResponseDto(updateReply);

        return responseDto;
    }

    @Transactional
    public void deleteReply(ReplySeqRequestDto requestDto) {
        replyRepository.deleteById(requestDto.getReplySeq());
    }
}
