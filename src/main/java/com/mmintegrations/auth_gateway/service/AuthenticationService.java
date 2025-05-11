package com.mmintegrations.auth_gateway.service;

import com.mmintegrations.auth_gateway.payload.request.AuthenticationRequest;
import com.mmintegrations.auth_gateway.payload.request.RegisterRequest;
import com.mmintegrations.auth_gateway.payload.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
}
