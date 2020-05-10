package com.common.repository;

import com.common.entity.Token;
import com.util.enums.TokenType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenRepository extends BaseRepository<Token>{
   List<Token> findByValueAndEmailAndDomainAndTokenTypeOrderByCreatedDateDesc(String token, String email, String domain, TokenType tokenType);
}
