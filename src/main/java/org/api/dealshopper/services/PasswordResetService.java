package org.api.dealshopper.services;


import lombok.RequiredArgsConstructor;
import org.api.dealshopper.domain.PasswordReset;
import org.api.dealshopper.domain.User;
import org.api.dealshopper.models.PasswordResetRequest;
import org.api.dealshopper.repositories.PasswordResetRepository;
import org.api.dealshopper.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;


@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private static final Map<String, LocalDateTime> resetTokens = new HashMap<>();
    private static final long RESET_TOKEN_EXPIRATION_MINUTES = 30;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetRepository passwordResetTokenRepository;

    public String generateResetToken() {
        return UUID.randomUUID().toString();
    }

    public void storeResetToken(String email, String resetToken) {
        resetTokens.put(resetToken, LocalDateTime.now().plusMinutes(RESET_TOKEN_EXPIRATION_MINUTES));
    }

    public boolean isResetTokenValid(String resetToken) {
        if (resetTokens.containsKey(resetToken)) {
            LocalDateTime expirationTime = resetTokens.get(resetToken);
            if (expirationTime.isAfter(LocalDateTime.now())) {
                return true;
            } else {
                resetTokens.remove(resetToken);
            }
        }
        return false;
    }

    public boolean requestPasswordReset(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional == null) {
            return false;
        }
        User user = userOptional.get();
        String token = UUID.randomUUID().toString();
        PasswordReset passwordResetToken = new PasswordReset(token, user);
        passwordResetTokenRepository.save(passwordResetToken);
        String resetLink = "http://localhost:8090/password/reset" + "?token=" + token;
        String message = "Hello " + user.getFirstName() + " " + user.getLastName() + ",\n\n"
                + "You have requested to reset your password. Please click the following link to reset your password:\n\n"
                + resetLink + "\n\n"
                + "If you did not request this reset, please ignore this email and your password will remain unchanged.\n\n"
                + "Thank you,\n"
                + "The Dealshopper Team";
        emailService.sendEmail(user.getEmail(), "Password Reset Request", message);
        return true;
    }


    public boolean resetPassword(String token, PasswordResetRequest newPassword) {
        Optional<PasswordReset> obj = passwordResetTokenRepository.findByToken(token);
        if (obj == null) {
            return false;
        }
        PasswordReset passwordResetToken = obj.get();
        if (passwordResetToken.getExpirationTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().isBefore(LocalDateTime.now())) {
            return false;
        }
        User user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword.getNewPassword()));
        userRepository.save(user);
        return true;


    }
}
