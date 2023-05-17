package org.api.dealshopper.controllers;

import org.api.dealshopper.models.EmailRequest;
import org.api.dealshopper.services.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EmailControllerTest {
    @Mock
    EmailService emailService;

    EmailController emailController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        emailController = new EmailController(emailService);
    }

    @Test
    void testSendEmailSuccess() {
        EmailRequest request = new EmailRequest();

        request.setTo("nicoletamihai151@gmail.com");
        request.setSubject("Test Email");
        request.setBody("This is a test email");

        ResponseEntity<?> responseEntity = emailController.sendEmail(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Email sent successfully", responseEntity.getBody());

        verify(emailService).sendEmail(request.getTo(), request.getSubject(), request.getBody());
    }

    @Test
    void testSendEmailServiceError() {
        EmailRequest request = new EmailRequest();

        request.setTo("nicoletamihai151@gmail.com");
        request.setSubject("Test Email");
        request.setBody("This is a test email");

        String errorMessage = "Failed to send email";
        doThrow(new RuntimeException("Failed to send email")).when(emailService).sendEmail(anyString(), anyString(), anyString());

        ResponseEntity<?> responseEntity = emailController.sendEmail(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Failed to send email", responseEntity.getBody());

    }
}
