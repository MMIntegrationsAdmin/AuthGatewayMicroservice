package com.mmintegrations.auth_gateway.service;

import com.mmintegrations.auth_gateway.entities.RefreshToken;
import com.mmintegrations.auth_gateway.payload.request.RefreshTokenRequest;
import com.mmintegrations.auth_gateway.payload.response.RefreshTokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(UUID userId);
    RefreshToken verifyExpiration(RefreshToken token);
    Optional<RefreshToken> findByToken(String token);
    RefreshTokenResponse generateNewToken(RefreshTokenRequest request);
    ResponseCookie generateRefreshTokenCookie(String token);
    String getRefreshTokenFromCookies(HttpServletRequest request);
    void deleteByToken(String token);
    ResponseCookie getCleanRefreshTokenCookie();
}
