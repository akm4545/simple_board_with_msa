package com.replyservice.replyservice.service;

import com.replyservice.replyservice.model.Reply;
import com.replyservice.replyservice.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReplyService {

    private final ReplyRepository replyRepository;

    public Reply selectReply(Integer replySeq) {
        return replyRepository.findById(replySeq).get();
    }

    public List<Reply> selectReplyList() {
        return replyRepository.findAll();
    }

    @Transactional
    public Reply insertReplyList(Reply reply) {
        return replyRepository.save(reply);
    }

    @Transactional
    public Reply updateReply(Reply reply) {
        Reply updateReply = replyRepository.findById(reply.getReplySeq()).get();

        updateReply.setReplyContent(reply.getReplyContent());

        return updateReply;
    }

    @Transactional
    public void deleteReply(Integer replySeq) {
        replyRepository.deleteById(replySeq);
    }
}
