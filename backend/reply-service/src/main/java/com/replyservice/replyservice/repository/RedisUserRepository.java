package com.replyservice.replyservice.repository;

import com.replyservice.replyservice.model.RedisUser;
import org.springframework.data.repository.CrudRepository;

public interface RedisUserRepository extends CrudRepository<RedisUser, String> {
}
