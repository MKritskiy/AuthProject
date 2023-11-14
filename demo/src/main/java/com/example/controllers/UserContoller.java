package com.example.controllers;

import com.example.models.securityModels.User;
import com.example.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserContoller {
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(){
        return new ResponseEntity<>(new UserService().findAll(), HttpStatus.OK);
    }
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestParam User user){
        return new ResponseEntity<>(new UserService().saveUser(user), HttpStatus.OK);
    }
}
