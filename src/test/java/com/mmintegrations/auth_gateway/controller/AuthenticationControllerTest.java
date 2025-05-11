package com.mmintegrations.auth_gateway.controller;

import com.mmintegrations.auth_gateway.enums.Role;
import com.mmintegrations.auth_gateway.payload.request.AuthenticationRequest;
import com.mmintegrations.auth_gateway.payload.request.RefreshTokenRequest;
import com.mmintegrations.auth_gateway.payload.request.RegisterRequest;
import com.mmintegrations.auth_gateway.payload.response.AuthenticationResponse;
import com.mmintegrations.auth_gateway.payload.response.RefreshTokenResponse;
import com.mmintegrations.auth_gateway.service.AuthenticationService;
import com.mmintegrations.auth_gateway.service.JwtService;
import com.mmintegrations.auth_gateway.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthenticationControllerTest {

    @Mock private AuthenticationService authenticationService;
    @Mock private RefreshTokenService refreshTokenService;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private JwtService jwtService;

    @InjectMocks private AuthenticationController controller;

    public AuthenticationControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_shouldReturnAuthenticationResponse() {
        RegisterRequest request = new RegisterRequest();
        UUID id = UUID.randomUUID();
        AuthenticationResponse response = AuthenticationResponse.builder()
                .id(id)
                .email("test@example.com")
                .role(Role.CLIENT)
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .tokenType("Bearer")
                .build();

        when(authenticationService.register(request)).thenReturn(response);
        when(jwtService.generateJwtCookie("access-token")).thenReturn(ResponseCookie.from("jwt", "access-token").build());
        when(refreshTokenService.generateRefreshTokenCookie("refresh-token")).thenReturn(ResponseCookie.from("refresh", "refresh-token").build());

        ResponseEntity<AuthenticationResponse> result = controller.register(request);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(response, result.getBody());
    }

    @Test
    void authenticate_shouldReturnAuthenticationResponse() {
        AuthenticationRequest request = new AuthenticationRequest();
        UUID id = UUID.randomUUID();
        AuthenticationResponse response = AuthenticationResponse.builder()
                .id(id)
                .email("test@example.com")
                .role(Role.CLIENT)
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .tokenType("Bearer")
                .build();

        when(authenticationService.authenticate(request)).thenReturn(response);
        when(jwtService.generateJwtCookie("access-token")).thenReturn(ResponseCookie.from("jwt", "access-token").build());
        when(refreshTokenService.generateRefreshTokenCookie("refresh-token")).thenReturn(ResponseCookie.from("refresh", "refresh-token").build());

        ResponseEntity<AuthenticationResponse> result = controller.authenticate(request);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(response, result.getBody());
    }

    @Test
    void refreshToken_shouldReturnNewToken() {
        RefreshTokenRequest request = new RefreshTokenRequest("refresh-token");
        RefreshTokenResponse response = RefreshTokenResponse.builder()
                .accessToken("new-access-token")
                .refreshToken("new-refresh-token")
                .tokenType("Bearer")
                .build();

        when(refreshTokenService.generateNewToken(request)).thenReturn(response);

        ResponseEntity<RefreshTokenResponse> result = controller.refreshToken(request);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(response, result.getBody());
    }

    @Test
    void refreshTokenCookie_shouldReturnNewJwtCookie() {
        HttpServletRequest httpRequest = mock(HttpServletRequest.class);
        when(refreshTokenService.getRefreshTokenFromCookies(httpRequest)).thenReturn("refresh-token");

        RefreshTokenResponse response = RefreshTokenResponse.builder()
                .accessToken("new-access-token")
                .refreshToken("new-refresh-token")
                .tokenType("Bearer")
                .build();

        when(refreshTokenService.generateNewToken(any())).thenReturn(response);
        when(jwtService.generateJwtCookie("new-access-token")).thenReturn(ResponseCookie.from("jwt", "new-access-token").build());

        ResponseEntity<Void> result = controller.refreshTokenCookie(httpRequest);

        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    void getAuthentication_shouldReturnAuthentication() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        Authentication result = controller.getAuthentication(request);

        assertEquals(authentication, result);
    }

    @Test
    void logout_shouldClearCookies() {
        HttpServletRequest httpRequest = mock(HttpServletRequest.class);
        when(refreshTokenService.getRefreshTokenFromCookies(httpRequest)).thenReturn("refresh-token");
        when(jwtService.getCleanJwtCookie()).thenReturn(ResponseCookie.from("jwt", "").build());
        when(refreshTokenService.getCleanRefreshTokenCookie()).thenReturn(ResponseCookie.from("refresh", "").build());

        ResponseEntity<Void> result = controller.logout(httpRequest);

        assertEquals(200, result.getStatusCodeValue());
    }
}
