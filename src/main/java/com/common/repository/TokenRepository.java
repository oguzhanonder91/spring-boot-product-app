package com.common.repository;

import com.common.entity.Token;
import com.util.enums.TokenType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends BaseRepository<Token> {
    void deleteAllByExpiryLessThan(long expiryDate);
}
