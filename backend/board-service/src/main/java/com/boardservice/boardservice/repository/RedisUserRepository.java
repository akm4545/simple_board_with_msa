package com.boardservice.boardservice.repository;

import com.boardservice.boardservice.model.RedisUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisUserRepository extends CrudRepository<RedisUser, String> {
}
