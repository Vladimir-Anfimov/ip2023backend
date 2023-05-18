package org.api.dealshopper.services;

import org.api.dealshopper.controllers.UserController;
import org.api.dealshopper.domain.User;
import org.api.dealshopper.models.AuthenticationRequest;
import org.api.dealshopper.models.AuthenticationResponse;
import org.api.dealshopper.models.RegisterRequest;
import org.api.dealshopper.models.RegisterResponse;
import org.api.dealshopper.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    JwtService jwtService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    AuthenticationManager authenticationManager;

    UserService userService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        this.userService = new UserService(null, jwtService, passwordEncoder, userRepository, authenticationManager);

    }

    @Test
    public void testLoginValidCredentials() {

        //creez un obiect valid AuthentificationRequest
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        String email = "user_existent@gmail.com";
        String password = "good_pass";
        authenticationRequest.setEmail(email);
        authenticationRequest.setPassword(password);

        // userul existent conform datelor corecte
        User existingUser = User.builder().email(email).build();
        existingUser.setPassword(passwordEncoder.encode(password));

        when(userRepository.findByEmail(authenticationRequest.getEmail())).thenReturn(Optional.of(existingUser));

        // tokenul  ce ar trebui returnat de JwtService
        String expectedToken = "token_ok";
        when(jwtService.generateToken(existingUser)).thenReturn(expectedToken);

        // Mock o authentificare cu succes
        when(authenticationManager.authenticate(any())).thenReturn(new UsernamePasswordAuthenticationToken(email, password));

        // aici folosesc login, incercarea de conectare
        AuthenticationResponse authenticationResponse = userService.login(authenticationRequest);

        // daca tokenul returnat se potriveste cu tokenul asteptat(token_ok) trece testul
        assertEquals(expectedToken, authenticationResponse.getToken());
    }


    @Test
    public void testLoginInvalidCredentials() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        String email = "user_inexistent@gmail.com";
        String password = "user_bad";
        authenticationRequest.setEmail(email);
        authenticationRequest.setPassword(password);

        // un user inexistent, in cazul in care era existent trebuia returnat un obiect de tip User,
        // ca in ex de la loginWithValidCredentials thenReturn(Optional.of(existingUser))
        when(userRepository.findByEmail(authenticationRequest.getEmail())).thenReturn(Optional.empty());

        // incerc sa ma conectez cu date incorecte
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid Credentials"));

        // daca exceptia BadCredentialsException este aruncata la incercarea de conectare, trece testul
        assertThrows(BadCredentialsException.class, () -> userService.login(authenticationRequest));
    }

    @Test
    public void testLoginAuthenticationFailure() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        String email = "user_ok@example.com";
        String password = "parola?";
        authenticationRequest.setEmail(email);
        authenticationRequest.setPassword(password);

        when(userRepository.findByEmail(authenticationRequest.getEmail())).thenReturn(Optional.of(User.builder().email(email).build()));

        // Scenariul testat este cel in care email-ul introdus este valid si utilizatorul este gasit in bd
        //dar este posibila o eroare la autentificare
        //de ex incercarea de autentificare cu un Token JWT expirat sau invalid

        when(authenticationManager.authenticate(any())).thenThrow(new RuntimeException("Authentication failed"));

        assertThrows(RuntimeException.class, () -> userService.login(authenticationRequest));
    }


    @Test
    public void testLoginUserSearchFailure() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        String email = "user@example.com";
        String password = "parola";
        authenticationRequest.setEmail(email);
        authenticationRequest.setPassword(password);

        //inlocuiesc userul ce ar trebui returnat cu exceptie Runtime ce se arunca cand cautarea  utilizatorului in bd nu este posibila
        when(userRepository.findByEmail(authenticationRequest.getEmail())).thenThrow(new RuntimeException("User search failed"));

        assertThrows(RuntimeException.class, () -> userService.login(authenticationRequest));
    }


    @Test
    public void testLoginTokenGenerationFailure() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        String email = "user@example.com";
        String password = "parola";
        authenticationRequest.setEmail(email);
        authenticationRequest.setPassword(password);
        User existingUser = User.builder().email(email).build();
        existingUser.setPassword(passwordEncoder.encode(password));
        when(userRepository.findByEmail(authenticationRequest.getEmail())).thenReturn(Optional.of(existingUser));

        when(authenticationManager.authenticate(any())).thenReturn(new UsernamePasswordAuthenticationToken(email, password));

        var message = "Token generation failed";
        when(jwtService.generateToken(existingUser)).thenThrow(new RuntimeException(message));

        assertThrows(RuntimeException.class, () -> userService.login(authenticationRequest), message);
    }

    @Test
    public void testLoginResponseContainsToken() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        String email = "user@example.com";
        String password = "parola";
        authenticationRequest.setEmail(email);
        authenticationRequest.setPassword(password);

        User existingUser = User.builder().email(email).build();
        existingUser.setPassword(passwordEncoder.encode(password));
        when(userRepository.findByEmail(authenticationRequest.getEmail())).thenReturn(Optional.of(existingUser));

        String expectedToken = "token_asteptat";
        when(jwtService.generateToken(existingUser)).thenReturn(expectedToken);

        when(authenticationManager.authenticate(any())).thenReturn(new UsernamePasswordAuthenticationToken(email, password));

        AuthenticationResponse authenticationResponse = userService.login(authenticationRequest);

        assertNotNull(authenticationResponse.getToken(), "Token should not be null");
        assertEquals(expectedToken, authenticationResponse.getToken(), "Token value should match expected value");
    }

    @Test
    public void testRegisterValidRequestSuccessfulRegistration() {
        // mock data
        RegisterRequest request = new RegisterRequest();
        request.setUsername("user");
        request.setFirstName("firstName");
        request.setLastName("lastName");
        request.setEmail("test@example.com");
        request.setPhone("0749999999");
        request.setPassword("password");
        request.setAddress("123 Main St");

        User savedUser = new User();
        savedUser.setId(1);
        savedUser.setEmail("test@example.com");
        savedUser.setPassword("password");
        savedUser.setPhone("0749999999");
        savedUser.setFirstName("first");
        savedUser.setLastName("last");
        savedUser.setAddress("12 Main St");
        savedUser.setUsername("username");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn("testToken");
        when(userRepository.findByEmail(savedUser.getEmail())).thenReturn((Optional.of(savedUser)));

        // Act
        RegisterResponse response = userService.register(request);

        verify(userRepository).save(argThat(user -> {
            return user.getUsername().equals("user") &&
                    user.getFirstName().equals("firstName") &&
                    user.getLastName().equals("lastName") &&
                    user.getEmail().equals("test@example.com") &&
                    user.getPhone().equals("0749999999") &&
                    user.getAddress().equals("123 Main St");
        }));

        verify(passwordEncoder).encode("password");
        verify(jwtService).generateToken(savedUser);
        assertEquals("testToken", response.getToken());

    }

    @Test
    void testRegisterUserAlreadyExists() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("user@example.com");
        registerRequest.setPassword("password");
        registerRequest.setFirstName("John");
        registerRequest.setLastName("Doe");
        registerRequest.setUsername("johndoe");
        registerRequest.setPhone("123-456-7890");
        registerRequest.setAddress("123 Main Street");

        when(userRepository.findByEmail(registerRequest.getEmail()))
                .thenReturn(Optional.of(User.builder()
                        .email(registerRequest.getEmail())
                        .build()));
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("User already exists"));

        assertThrows(RuntimeException.class, () -> userService.register(registerRequest));
    }
}
