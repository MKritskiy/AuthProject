package com.example.PR.controllers;

import com.example.PR.dto.AuthenticationRequest;
import com.example.PR.dto.AuthenticationResponse;
import com.example.PR.dto.RegisterRequest;
import com.example.PR.dto.SetRoleRequest;
import com.example.PR.security.jwt.JwtTokenProvider;
import com.example.PR.services.IAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/api")
public class AuthenticationController {
    private final IAuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(IAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request)
    {
        return new ResponseEntity<>(authenticationService.authenticate(request), HttpStatus.OK);
    }
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request)
    {
        return new ResponseEntity<>(authenticationService.register(request), HttpStatus.OK);
    }

    @PatchMapping("/set-role")
    public ResponseEntity<String> setRole(@RequestBody SetRoleRequest request)
    {
        return new ResponseEntity<>(authenticationService.setRole(request), HttpStatus.OK);
    }

}
