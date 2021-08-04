package com.common.repository;

import com.common.entity.Token;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends BaseRepository<Token> {
    void deleteAllByExpiryLessThan(long expiryDate);
}
