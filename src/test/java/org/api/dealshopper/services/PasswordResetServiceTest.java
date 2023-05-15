package org.api.dealshopper.services;

import static org.api.dealshopper.services.PasswordResetService.RESET_TOKEN_EXPIRATION_MINUTES;
import static org.junit.jupiter.api.Assertions.*;

import org.api.dealshopper.domain.PasswordReset;
import org.api.dealshopper.domain.User;
import org.api.dealshopper.models.PasswordResetRequest;
import org.api.dealshopper.repositories.PasswordResetRepository;
import org.api.dealshopper.repositories.UserRepository;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PasswordResetServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PasswordResetRepository passwordResetTokenRepository;

    private PasswordResetService passwordResetService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        passwordResetService = new PasswordResetService(userRepository, emailService, passwordEncoder, passwordResetTokenRepository);
    }

    @Test
    public void requestPasswordReset_shouldReturnTrue_whenUserExists() {
        // Arrange
        User user = new User();
        user.setEmail("user@example.com");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordResetTokenRepository.save(any(PasswordReset.class))).thenReturn(new PasswordReset());

        // Act
        boolean result = passwordResetService.requestPasswordReset(user.getEmail());

        // Assert
        assertTrue(result,"");
    }

    @Test
    public void requestPasswordReset_shouldReturnFalse_whenUserDoesNotExist() {
        // Arrange
        String email = "user@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        boolean result = passwordResetService.requestPasswordReset(email);

        // Assert
        assertFalse(result,"");
    }

    @Test
    public void resetPassword_shouldReturnTrue_whenTokenIsValid() {
        // Arrange
        User user = new User();
        user.setEmail("user@example.com");
        String password = "newPassword123";
        PasswordResetRequest passwordResetRequest = new PasswordResetRequest(user.getEmail(),password);

        String token = UUID.randomUUID().toString();
        PasswordReset passwordResetToken = new PasswordReset(token, user);
        LocalDateTime now = LocalDateTime.now();
        passwordResetToken.setExpirationTime(Date.from(now.plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant()));
        when(passwordResetTokenRepository.findByToken(token)).thenReturn(Optional.of(passwordResetToken));
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        // Act
        boolean result = passwordResetService.resetPassword(token, passwordResetRequest);

        // Assert
        assertTrue(result,"");
        verify(userRepository).save(user);
    }

    @Test
    public void resetPassword_shouldReturnFalse_whenTokenIsInvalid() {
        // Arrange
        User user = new User();
        user.setEmail("user@example.com");
        String password = "newPassword123";
        PasswordResetRequest passwordResetRequest = new PasswordResetRequest(user.getEmail(),password);
        String token = UUID.randomUUID().toString();
        when(passwordResetTokenRepository.findByToken(token)).thenReturn(Optional.empty());

        // Act
        boolean result = passwordResetService.resetPassword(token, passwordResetRequest);

        // Assert
        assertFalse(result,"");
    }

    @Test
    public void resetPassword_shouldReturnFalse_whenTokenIsExpired() {
        // Arrange
        User user = new User();
        user.setEmail("user@example.com");
        String password = "newPassword123";
        PasswordResetRequest passwordResetRequest = new PasswordResetRequest();
        passwordResetRequest.setNewPassword(password);
        passwordResetRequest.setEmail(user.getEmail());

        LocalDateTime expirationTime = LocalDateTime.now().minusMinutes(RESET_TOKEN_EXPIRATION_MINUTES).minusSeconds(1);
        Date expirationDate = Date.from(expirationTime.atZone(ZoneId.systemDefault()).toInstant());
        PasswordReset passwordReset = new PasswordReset("randomToken", user, expirationDate);
        passwordResetTokenRepository.save(passwordReset);

        // Act
        boolean result = passwordResetService.resetPassword(passwordReset.getToken(), passwordResetRequest);

        // Assert
        Assertions.assertFalse(result);
        User updatedUser = userRepository.findByEmail(user.getEmail()).orElse(null);
        assertNull(updatedUser,"");
    }

}
