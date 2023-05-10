package org.api.dealshopper.controllers;

import lombok.RequiredArgsConstructor;
import org.api.dealshopper.models.*;
import org.api.dealshopper.services.PasswordResetService;
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
    private final PasswordResetService passwordResetService;

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

    @PostMapping("/password/reset/request")
    public ResponseEntity<String> requestPasswordReset(@RequestParam String email) {
        boolean result = passwordResetService.requestPasswordReset(email);
        if (result) {
            return ResponseEntity.ok("Password reset email sent.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    @PostMapping("/password/reset/confirm")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestBody PasswordResetRequest request) {
        boolean result = passwordResetService.resetPassword(token, request);
        if (result) {
            return ResponseEntity.ok("Password reset successful.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid reset token.");
        }
    }
}
