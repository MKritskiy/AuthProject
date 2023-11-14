package com.example.PR.security.jwt;

import com.example.PR.models.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.DigestAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecureDigestAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    private SecretKey secret;

    @Value("${jwt.token.expired}")
    private long validityInMilliseconds;

    @Autowired
    private UserDetailsService userDetailsService;

    public JwtTokenProvider( @Value("${jwt.token.secret}") String secret) {
        this.secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String createToken(String username, List<Role> roles){
        Claims claims = Jwts.claims()
                .subject(username)
                .add("roles",  getRoleNames(roles))
                .build();

        Date now = new Date();
        Date validity = new Date(now.getTime()+validityInMilliseconds);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(validity)
                .signWith(secret)
                .compact();
    }

    public Authentication getAuthentication(String token){
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token){
        return Jwts.parser().verifyWith(secret).build().parseSignedClaims(token).getPayload().getSubject();
    }


    public boolean validateToken(String token) throws JwtAuthenticationException {
        try {
            Jws<Claims> claims = Jwts.parser().verifyWith(secret).build().parseSignedClaims(token);

            if (claims.getPayload().getExpiration().before(new Date())){
                return false;
            }

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("JWT token is expired or invalid");
        }

    }

    public String resolveToken(HttpServletRequest req){
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken!=null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }

    private List<String> getRoleNames(List<Role> userRoles){
        List<String> result = new ArrayList<>();

        userRoles.forEach(role-> {
            result.add(role.getName());
        });

        return result;
    }
}
