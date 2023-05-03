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
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<?> login(
            @RequestBody AuthenticationRequest request
    )
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

    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request
    )
    {
        try {
            RegisterResponse registerResponse = service.register(request);
            return ResponseEntity.ok(registerResponse);
        }
        catch (DataIntegrityViolationException e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User already exists");
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
