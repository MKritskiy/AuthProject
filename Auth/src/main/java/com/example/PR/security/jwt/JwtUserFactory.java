package com.example.PR.security.jwt;


import com.example.PR.models.Role;
import com.example.PR.models.Status;
import com.example.PR.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
public class JwtUserFactory {
    public JwtUserFactory() {
    }

    public static JwtUser create(User user){
        log.info("IN create user");
        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getStatus().equals(Status.ACTIVE),
                user.getUpdated(),
                user.getRoles()
        );
    }

}
