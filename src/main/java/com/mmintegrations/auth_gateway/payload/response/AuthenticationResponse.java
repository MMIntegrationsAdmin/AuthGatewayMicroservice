package com.mmintegrations.auth_gateway.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mmintegrations.auth_gateway.enums.Role;
import com.mmintegrations.auth_gateway.enums.TokenType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private UUID id;
    private String email;
    private Role role;

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("token_type")
    private String tokenType;

}
