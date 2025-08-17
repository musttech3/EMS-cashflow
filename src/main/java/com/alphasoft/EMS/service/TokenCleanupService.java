package com.alphasoft.EMS.service;

import com.alphasoft.EMS.repository.TokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TokenCleanupService {
    private final TokenRepository tokenRepository;

    @Scheduled(cron = "0 0 3 * * ?") // 3 صباحاً كل يوم
    @Transactional
    public void cleanupExpiredTokens() {
        // حذف التوكنات المنتهية أو الملغاة
        tokenRepository.deleteByExpiryDateBeforeOrRevokedTrue(Instant.now());
    }
}
