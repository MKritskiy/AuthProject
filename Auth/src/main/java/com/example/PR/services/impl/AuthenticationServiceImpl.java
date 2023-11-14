package com.example.PR.services.impl;

import com.example.PR.dto.AuthenticationRequest;
import com.example.PR.dto.AuthenticationResponse;
import com.example.PR.dto.RegisterRequest;
import com.example.PR.dto.SetRoleRequest;
import com.example.PR.models.User;
import com.example.PR.security.jwt.JwtTokenProvider;
import com.example.PR.security.redis.RedisTokenStore;
import com.example.PR.services.IAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticationServiceImpl implements IAuthenticationService {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private RedisTokenStore redisTokenStore;

    @Override
    public AuthenticationResponse register(RegisterRequest registerRequest) {
        log.info("IN register");

        User user  = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();
        userService.register(user);
        var jwtToken = jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
        redisTokenStore.storeToken(user.getUsername(), jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest registerRequest) {
        log.info("IN authenticate username: {}, {}", registerRequest.getUsername(), registerRequest.getPassword());

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(registerRequest.getUsername(), registerRequest.getPassword()));

        log.info("AFTER authenticationManager.authenticate");

        User user = userService.findByUsername(registerRequest.getUsername());

        var jwtToken = jwtTokenProvider.createToken(user.getUsername(), user.getRoles());

        log.info("user: {}", user);
        log.info("token: {}", jwtToken);
        if (redisTokenStore.getToken(user.getUsername()) != null){
            redisTokenStore.deleteToken(user.getUsername());
        }
        redisTokenStore.storeToken(user.getUsername(), jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public String setRole(SetRoleRequest setRoleRequest) {
        User user = userService.findByUsername(setRoleRequest.getUsername());
        userService.setRole(user, setRoleRequest.getRole());
        return "Role successful set";
    }

}
