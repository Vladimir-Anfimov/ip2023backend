package org.api.dealshopper.controllers;

import org.api.dealshopper.controllers.UserController;
import org.api.dealshopper.models.AuthenticationRequest;
import org.api.dealshopper.models.AuthenticationResponse;
import org.api.dealshopper.models.RegisterRequest;
import org.api.dealshopper.models.RegisterResponse;
import org.api.dealshopper.repositories.UserRepository;
import org.api.dealshopper.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


class UserControllerTest {
    @Mock
    UserService userService;

    @Autowired
    private MockMvc mockMvc;

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
        @Test
        public void testRegisterSuccess() {
            RegisterRequest request = new RegisterRequest();
            request.setUsername("username");
            request.setFirstName("firstName");
            request.setLastName("lastName");
            request.setPhone("0749999999");
            request.setEmail("test@example.com");
            request.setPassword("password");
            request.setAddress("123 Main St");

            RegisterResponse expectedResponse = new RegisterResponse();
            expectedResponse.setToken("expectedToken");

            when(userService.register(request)).thenReturn(expectedResponse);

            ResponseEntity<?> response = userController.register(request);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(expectedResponse, response.getBody());
        }
    @Test
    public void testRegisterUserAlreadyExists() {
        // Create mock service that throws a DataIntegrityViolationException
     //   RegisterService service = mock(RegisterService.class);
        when(userService.register(any())).thenThrow(new DataIntegrityViolationException("User already exists"));
        // Create an instance of the RegisterController class
   //     RegisterController controller = new RegisterController(service);

        // Create a RegisterRequest object with valid input data
        RegisterRequest request = new RegisterRequest();
        request.setUsername("username");
        request.setFirstName("firstName");
        request.setLastName("lastName");
        request.setPhone("0749999999");
        request.setEmail("test@example.com");
        request.setPassword("password");
        request.setAddress("123 Main St");

        // Call the register method with the valid input data
        ResponseEntity<?> response = userController.register(request);

        // Verify that the controller returns the expected error message and status code
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("User already exists", response.getBody());
    }

    @Test
    public void testRegisterUnknownError() {
        // Create mock service that throws an exception
     //   RegisterService service = mock(RegisterService.class);
        when(userService.register(any())).thenThrow(new RuntimeException("Unknown error occurred"));
        // Create an instance of the RegisterController class
//        RegisterController controller = new RegisterController(service);

        // Create a RegisterRequest object with valid input data
        RegisterRequest request = new RegisterRequest();
        request.setUsername("username");
        request.setFirstName("firstName");
        request.setLastName("lastName");
        request.setPhone("0749999999");
        request.setEmail("test@example.com");
        request.setPassword("password");
        request.setAddress("123 Main St");

        // Call the register method with the valid input data
        ResponseEntity<?> response = userController.register(request);

        // Verify that the controller returns the expected error message and status code
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Unknown error occurred", response.getBody());
    }
/*
        @Test
        public void testRegisterUserAlreadyExists() {
            RegisterRequest request = new RegisterRequest("johndoe@example.com", "pass123");

            when(userService.register(request)).thenThrow(new DataIntegrityViolationException("User already exists"));

            ResponseEntity<?> response = controller.register(request);

            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            assertEquals("User already exists", response.getBody());
        }

        @Test
        public void testRegisterUnknownError() {
            RegisterRequest request = new RegisterRequest("johndoe@example.com", "pass123");

            when(service.register(request)).thenThrow(new RuntimeException("Unknown error"));

            ResponseEntity<?> response = controller.register(request);

            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            assertEquals("Unknown error", response.getBody());
        }

    }
*/
}