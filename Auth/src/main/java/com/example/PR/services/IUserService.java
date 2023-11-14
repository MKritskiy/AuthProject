package com.example.PR.services;

import com.example.PR.models.User;

import java.util.List;

public interface IUserService {
    User register(User user);

    List<User> getAll();

    User findByUsername(String username);

    User findById(Long id);

    void delete(Long id);
}
