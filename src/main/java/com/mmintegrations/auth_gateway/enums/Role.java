package com.mmintegrations.auth_gateway.enums;

import org.springframework.security.core.GrantedAuthority;


public enum Role implements GrantedAuthority {
    CLIENT, TECHNICIAN, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
