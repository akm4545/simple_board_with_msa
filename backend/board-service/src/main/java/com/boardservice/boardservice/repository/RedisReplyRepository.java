package com.boardservice.boardservice.repository;

import com.boardservice.boardservice.model.RedisReply;
import org.springframework.data.repository.CrudRepository;

public interface RedisReplyRepository extends CrudRepository<RedisReply, String> {
}
