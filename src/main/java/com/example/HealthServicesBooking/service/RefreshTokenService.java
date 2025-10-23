package com.example.HealthServicesBooking.service;

import com.example.HealthServicesBooking.entity.RefreshToken;
import com.example.HealthServicesBooking.entity.User;
import com.example.HealthServicesBooking.exception.UnauthorizedException;
import com.example.HealthServicesBooking.repository.RefreshTokenRepository;
import com.example.HealthServicesBooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Value("${app.jwt.refresh-expiration}")
    private Long refreshTokenDurationMs;

    /**
     * Create refresh token for user
     */
    @Transactional
    public RefreshToken createRefreshToken(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        // Delete old refresh token if exists
        refreshTokenRepository.findByUser(user).ifPresent(refreshTokenRepository::delete);

        // Create new refresh token
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plusSeconds(refreshTokenDurationMs / 1000))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    /**
     * Find refresh token by token string
     */
    @Transactional(readOnly = true)
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new UnauthorizedException("Refresh token not found"));
    }

    /**
     * Verify refresh token expiration
     */
    @Transactional
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.isExpired()) {
            refreshTokenRepository.delete(token);
            throw new UnauthorizedException("Refresh token was expired. Please make a new login request");
        }
        return token;
    }

    /**
     * Delete refresh token
     */
    @Transactional
    public void deleteByUserId(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("User not found"));
        refreshTokenRepository.deleteByUser(user);
    }

    /**
     * Delete expired tokens (scheduled task)
     */
    @Transactional
    public void deleteExpiredTokens() {
        log.info("Deleting expired refresh tokens");
        refreshTokenRepository.deleteExpiredTokens(LocalDateTime.now());
    }
}

