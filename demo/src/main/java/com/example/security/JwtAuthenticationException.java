package com.example.security;

public class JwtAuthenticationException extends Exception {
    public JwtAuthenticationException(String message) {
        super(message);
    }
}
