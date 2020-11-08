package com.common.repository;

import com.common.entity.User;
import com.util.enums.EntityState;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<User> {
    User findByEmailAndEntityState(final String email, EntityState state);
    User findByIdAndEntityState(final String id, EntityState state);
}

