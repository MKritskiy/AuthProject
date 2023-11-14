package com.example.configuration;


import com.example.security.ITokenChecker;
import com.example.security.JwtAuthenticationException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private ITokenChecker tokenChecker;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = tokenChecker.resolveToken((HttpServletRequest) request);
        try {
            if (token!=null && tokenChecker.validateToken(token)) {
                Authentication auth = tokenChecker.getAuthentication(token);
                if (auth!=null) {
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (JwtAuthenticationException | ExpiredJwtException | MalformedJwtException e) {
            e.getMessage();
        } finally {

            filterChain.doFilter(request,response);
        }
    }
}
