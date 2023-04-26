package org.api.dealshopper.controllers;

import lombok.RequiredArgsConstructor;
import org.api.dealshopper.models.AuthenticationRequest;
import org.api.dealshopper.models.AuthenticationResponse;
import org.api.dealshopper.models.RegisterRequest;
import org.api.dealshopper.models.RegisterResponse;
import org.api.dealshopper.services.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/security")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody AuthenticationRequest request)
    {
        try {
            AuthenticationResponse authenticationResponse = service.login(request);
            return ResponseEntity.ok(authenticationResponse);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request)
    {
        try {
            RegisterResponse registerResponse = service.register(request);
            return ResponseEntity.ok(registerResponse);
        }
        catch (DataIntegrityViolationException e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username already exists");
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
