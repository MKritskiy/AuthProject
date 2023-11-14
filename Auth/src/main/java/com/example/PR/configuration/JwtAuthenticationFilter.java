package com.example.PR.configuration;

import com.example.PR.security.JwtUserDetailsService;
import com.example.PR.security.jwt.JwtAuthenticationException;
import com.example.PR.security.jwt.JwtTokenProvider;
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
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        try {
            if (token!=null && jwtTokenProvider.validateToken(token)) {
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                if (auth!=null) {
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (JwtAuthenticationException e) {
            e.getMessage();
        } finally {
            log.info("IN doFilterInternal");

            filterChain.doFilter(request,response);
        }
    }
}
