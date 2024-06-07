package com.replyservice.replyservice.repository;

import com.replyservice.replyservice.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Integer> {
    List<Reply> findByBoardSeq(Integer boardSeq);
}
