package com.example.PR.services;

import com.example.PR.dto.AuthenticationRequest;
import com.example.PR.dto.AuthenticationResponse;
import com.example.PR.dto.RegisterRequest;
import com.example.PR.dto.SetRoleRequest;

public interface IAuthenticationService {
    AuthenticationResponse register(RegisterRequest registerRequest);
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);
    String setRole(SetRoleRequest setRoleRequest);
}
