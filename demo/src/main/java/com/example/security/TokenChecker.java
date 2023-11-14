package com.example.security;

import com.example.models.securityModels.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TokenChecker implements ITokenChecker {
    @Autowired
    private RedisTokenStore redisTokenStore;

    private SecretKey secret;
    public TokenChecker( @Value("${jwt.token.secret}") String secret) {
        this.secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public Authentication getAuthentication(String token) throws ExpiredJwtException, MalformedJwtException
    {
        log.info("IN getAuthentication role: {}", extractRoles(token));
        return new UsernamePasswordAuthenticationToken(extractUsername(token), "", extractRoles(token));
    }

    public List<GrantedAuthority> extractRoles(String token) throws ExpiredJwtException, MalformedJwtException
    {
        List<String> roles = extractClaim(token, claims -> claims.get("roles", List.class));
        log.info("IN extractRoles roles: {}", roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()));
        return  roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public String extractUsername(String token) throws ExpiredJwtException, MalformedJwtException
    {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) throws ExpiredJwtException, MalformedJwtException
    {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws ExpiredJwtException, MalformedJwtException
    {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) throws ExpiredJwtException, MalformedJwtException
    {

        return Jwts
                .parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }



    public boolean validateToken(String token) throws JwtAuthenticationException {
        try {
            //get payload from jwt java jjwt without verifyWith
            log.info("IN validateToken parsedToken: {}", Jwts.parser().verifyWith(secret).build().parseClaimsJws(token).getBody());


            //if token isn't in redis database
            if (redisTokenStore.getToken(extractUsername(token))==null){
                return false;
            }

            if (extractExpiration(token).before(new Date())){
                return false;
            }

            return true;
        } catch (Exception e)
        {
            log.error(e.getMessage());
            throw new JwtAuthenticationException(e.getMessage());
        }
//        catch (JwtException | IllegalArgumentException e) {
//            throw new JwtAuthenticationException("JWT token is expired or invalid");
//        }

    }

    public String resolveToken(HttpServletRequest req){
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken!=null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }

}
