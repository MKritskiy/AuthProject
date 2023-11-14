package com.example.services;

import com.example.models.securityModels.User;
import com.example.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Transactional
    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        log.info("IN findByUsername - user: {} found by username: {}", user, username);
        return user;
    }
    @Transactional
    public List<User> findAll(){
        return userRepository.findAll();
    }
    @Transactional
    public User saveUser(User user){
        return userRepository.save(user);
    }
}
