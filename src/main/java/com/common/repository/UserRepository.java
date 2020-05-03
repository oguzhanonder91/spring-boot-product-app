package com.common.repository;

import com.common.entity.User;
import org.springframework.stereotype.Repository;

/**
 * Created by oguzhanonder - 18.10.2018
 */
@Repository
public interface UserRepository extends BaseRepository<User> {
    User findByEmail(String email);
}

