package org.api.dealshopper.controllers;

import org.api.dealshopper.models.PasswordResetRequest;
import org.api.dealshopper.services.PasswordResetService;
import org.api.dealshopper.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private PasswordResetService passwordResetService;

    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userController=new UserController(userService,passwordResetService);
    }

    @Test
    public void testRequestPasswordResetSuccess() {
        String email = "test@example.com";

        when(passwordResetService.requestPasswordReset(email)).thenReturn(true);

        ResponseEntity<String> response = userController.requestPasswordReset(email);

        verify(passwordResetService).requestPasswordReset(email);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password reset email sent.", response.getBody());
    }

    @Test
    public void testRequestPasswordResetNotFound() {
        String email = "test@example.com";

        when(passwordResetService.requestPasswordReset(email)).thenReturn(false);

        ResponseEntity<String> response = userController.requestPasswordReset(email);

        verify(passwordResetService).requestPasswordReset(email);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found.", response.getBody());
    }

    @Test
    public void testResetPasswordSuccess() {
        String token = "123";
        PasswordResetRequest request = new PasswordResetRequest("password", "user@example.com");

        when(passwordResetService.resetPassword(token, request)).thenReturn(true);

        ResponseEntity<String> response = userController.resetPassword(token, request);

        verify(passwordResetService).resetPassword(token, request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password reset successful.", response.getBody());
    }

    @Test
    public void testResetPasswordInvalidToken() {
        String token = "123";
        PasswordResetRequest request = new PasswordResetRequest("password", "user@example.com");

        when(passwordResetService.resetPassword(token, request)).thenReturn(false);

        ResponseEntity<String> response = userController.resetPassword(token, request);

        verify(passwordResetService).resetPassword(token, request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid reset token.", response.getBody());
    }
}
