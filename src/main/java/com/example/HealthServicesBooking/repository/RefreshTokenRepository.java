package com.example.HealthServicesBooking.repository;

import com.example.HealthServicesBooking.entity.RefreshToken;
import com.example.HealthServicesBooking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    /**
     * Find refresh token by token string
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Find refresh token by user
     */
    Optional<RefreshToken> findByUser(User user);

    /**
     * Delete all refresh tokens for a user
     */
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.user = :user")
    void deleteByUser(@Param("user") User user);

    /**
     * Delete expired tokens
     */
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiryDate < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);

    /**
     * Check if token exists and is not expired
     */
    @Query("SELECT CASE WHEN COUNT(rt) > 0 THEN true ELSE false END FROM RefreshToken rt WHERE rt.token = :token AND rt.expiryDate > :now")
    boolean existsByTokenAndNotExpired(@Param("token") String token, @Param("now") LocalDateTime now);
}

