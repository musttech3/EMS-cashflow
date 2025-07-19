package com.alphasoft.EMS.repository;


import com.alphasoft.EMS.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("SELECT t FROM Token t WHERE t.jti = :jti AND t.username = :username AND t.expiryDate > :now AND t.revoked = false")
    List<Token> findValidToken(@Param("jti") String jti,
                               @Param("username") String username,
                               @Param("now") Instant now
    );

    // للحذف المجدول
    @Modifying
    @Query("DELETE FROM Token t WHERE t.expiryDate < :now OR t.revoked = true")
    void deleteByExpiryDateBeforeOrRevokedTrue(@Param("now") Instant now);


    @Query("SELECT t FROM Token t WHERE t.jti = :jti AND t.username = :username")
    Optional<Token> findByJtiAndUsername(
            @Param("jti") String jti,
            @Param("username") String username
    );

    @Modifying
    @Query("DELETE FROM Token t WHERE t.username = :username")
    void deleteTokensByUsername(@Param("username") String username);

    @Modifying
    @Query("DELETE FROM Token t WHERE t.username = :username AND t.userAgent = :userAgent")
    void deleteOldTokens(
            @Param("username") String username,
            @Param("userAgent") String userAgent
    );
}
