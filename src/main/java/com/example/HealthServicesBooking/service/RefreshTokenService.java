package com.example.HealthServicesBooking.service;

import com.example.HealthServicesBooking.entity.RefreshToken;
import com.example.HealthServicesBooking.entity.User;
import com.example.HealthServicesBooking.exception.BadRequestException;
import com.example.HealthServicesBooking.repository.RefreshTokenRepository;
import com.example.HealthServicesBooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    
    @Value("${app.jwt.refresh-expiration}")
    private Long refreshTokenDurationMs;
    
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
    
    @Transactional
    public RefreshToken createRefreshToken(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));
        
        // Delete old refresh token if exists
        refreshTokenRepository.findByUser(user).ifPresent(oldToken -> {
            refreshTokenRepository.delete(oldToken);
            refreshTokenRepository.flush(); // Force delete before insert
        });
        
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .build();
        
        return refreshTokenRepository.save(refreshToken);
    }
    
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new BadRequestException("Refresh token đã hết hạn. Vui lòng đăng nhập lại.");
        }
        return token;
    }
    
    @Transactional
    public int deleteByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));
        return refreshTokenRepository.deleteByUser(user);
    }
}

