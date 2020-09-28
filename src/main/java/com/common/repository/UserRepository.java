package com.common.repository;

import com.common.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends BaseRepository<User> {
    User findByEmail(final String email);
}

