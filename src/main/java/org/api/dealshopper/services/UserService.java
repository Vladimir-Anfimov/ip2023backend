package org.api.dealshopper.services;

import lombok.RequiredArgsConstructor;
import org.api.dealshopper.domain.User;
import org.api.dealshopper.models.AuthenticationRequest;
import org.api.dealshopper.models.AuthenticationResponse;
import org.api.dealshopper.models.RegisterRequest;
import org.api.dealshopper.models.RegisterResponse;
import org.api.dealshopper.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final AuthenticationService authService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final AuthenticationManager authenticationManager;

    /**
     * @return the generated token if the credentials are ok
     */
    public AuthenticationResponse login(AuthenticationRequest request) {

        UsernamePasswordAuthenticationToken upaToken = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        );

        authenticationManager.authenticate(upaToken);

        var user = repository.findByEmail(request.getEmail()).orElseThrow();

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    /**
     * saves the user in the database
     */
    public RegisterResponse register(RegisterRequest request) {

        var user = User.builder()
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .address(request.getAddress())
                .build();


        var savedUser = repository.save(user);
        /*
        var jwtToken = jwtService.generateToken(savedUser);
        */

        var jwtToken = login(AuthenticationRequest.builder()
                        .email(request.getEmail())
                        .password(request.getPassword())
                        .build()
                )
                .getToken();

        return RegisterResponse.builder()
                .token(jwtToken)
                .build();
    }
}
