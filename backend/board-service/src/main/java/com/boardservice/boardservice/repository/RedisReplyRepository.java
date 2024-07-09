package com.boardservice.boardservice.repository;

import com.boardservice.boardservice.model.RedisReply;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RedisReplyRepository extends CrudRepository<RedisReply, String> {
    List<RedisReply> findByBoardSeq(Integer boardSeq);
}
