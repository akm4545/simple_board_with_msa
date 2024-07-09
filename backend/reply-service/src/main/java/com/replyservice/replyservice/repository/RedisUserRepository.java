package com.replyservice.replyservice.repository;

import com.replyservice.replyservice.model.RedisUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisUserRepository extends CrudRepository<RedisUser, String> {
}
