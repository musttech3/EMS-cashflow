package com.alphasoft.EMS.service;

import com.alphasoft.EMS.model.Token;
import com.alphasoft.EMS.repository.TokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenService {

    TokenRepository tokenRepository;

    @Autowired
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public void saveToken(Token token) {
        tokenRepository.save(token);
    }

    public Token findByJtiAndUsername(String jti, String username) {
        return tokenRepository.findByJtiAndUsername(jti, username).orElse(null);
    }

    public Token findValidToken(String jti, String username) {
        return tokenRepository.findValidToken(jti, username, Instant.now()).stream().findFirst().orElse(null);
    }

    public void revokeToken(String jti, String username) {
        Token token = tokenRepository.findByJtiAndUsername(jti, username).orElse(null);
        if (token != null) {
            token.setRevoked(true);
            token.setLogoutTime(Instant.now());
            tokenRepository.save(token);
        }
    }

    @Transactional
    public void deleteToken(Token token) {
        tokenRepository.delete(token);
    }

    @Transactional
    public void deleteTokensByUsername(String username) {
        tokenRepository.deleteTokensByUsername(username);
    }

    @Transactional
    public void deleteOldTokens(String username, String deviceId) {
        tokenRepository.deleteOldTokens(username, deviceId);
    }

}
