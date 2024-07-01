package ru.kata.spring.boot_security.demo.security;

import org.springframework.security.core.GrantedAuthority;

public class GrantedAuthorityImpl implements GrantedAuthority {
    private final String role;

    public GrantedAuthorityImpl(String name) {
        this.role = name;
    }

    @Override
    public String getAuthority() {
        return role;
    }
}
