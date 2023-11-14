package com.example.PR.services.impl;

import com.example.PR.models.Role;
import com.example.PR.models.User;
import com.example.PR.repositories.RoleRepository;
import com.example.PR.repositories.UserRepository;
import com.example.PR.services.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@Slf4j
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User setRole(User user, String roleName) {
        Role role = roleRepository.findByName(roleName);
        if (!user.getRoles().contains(role))
            user.getRoles().add(role);
        return userRepository.save(user);
    }
    @Override
    public User register(User user) {
        Role role = roleRepository.findByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(role);

        user.setPassword(user.getPassword());
        user.setRoles(userRoles);

        User registeredUser = userRepository.save(user);
        log.info("IN register - user: {} successfully registered", registeredUser);
        return registeredUser;
    }

    @Override
    public List<User> getAll() {
        List<User> users =userRepository.findAll();
        log.info("IN getAll - {} users found", users.size());
        return users;
    }

    @Override
    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        log.info("IN findByUsername - user: {} found by username: {}", user, username);
        return user;
    }

    @Override
    public User findById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            log.warn("IN findById - no user found by id: {}", id);
            return null;
        }
        log.info("IN findById - user: {} found by id", user);
        return user;
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
        log.info("IN delete - user with id: {} successfully deleted", id);
    }
}
