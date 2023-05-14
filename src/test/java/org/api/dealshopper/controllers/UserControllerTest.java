package org.api.dealshopper.controllers;

import org.api.dealshopper.controllers.UserController;
import org.api.dealshopper.models.AuthenticationRequest;
import org.api.dealshopper.models.AuthenticationResponse;
import org.api.dealshopper.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


class UserControllerTest {
    @Mock
    UserService userService;

    UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userService, null);
    }

    @Test
    void testLoginSuccess() {
        String email = "user@example.com";
        String password = "password";
        String token = "token_asteptat";

        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail(email);
        request.setPassword(password);

        AuthenticationResponse expectedResponse = AuthenticationResponse.builder()
                .token(token)
                .build();

        when(userService.login(any())).thenReturn(expectedResponse);

        ResponseEntity<?> responseEntity = userController.login(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof AuthenticationResponse);

        AuthenticationResponse response = (AuthenticationResponse) responseEntity.getBody();
        assertEquals(token, response.getToken());

    }

    @Test
    void testLoginFailure() {
        String email = "user@example.com";
        String password = "parola";

        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail(email);
        request.setPassword(password);

        String errorMessage = "Invalid credentials";
        when(userService.login(any())).thenThrow(new RuntimeException(errorMessage));

        ResponseEntity<?> responseEntity = userController.login(request);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(errorMessage, responseEntity.getBody());
    }

    @Test
    void testLoginException() throws Exception {
        String email = "user@example.com";
        String password = "parola";
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail(email);
        request.setPassword(password);

        String errorMessage = "Failed to authenticate user";
        when(userService.login(request)).thenThrow(new RuntimeException(errorMessage));

        ResponseEntity<?> responseEntity = userController.login(request);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(errorMessage, responseEntity.getBody());

    }
}