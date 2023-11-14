package com.example.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface ITokenChecker {
    String resolveToken(HttpServletRequest req);
    boolean validateToken(String token) throws JwtAuthenticationException;
    Authentication getAuthentication(String token);
}