package com.keplars.examples.controller;

import com.keplars.examples.service.MarketingEmailService;
import com.keplars.examples.service.TransactionalEmailService;
import com.keplars.models.SendEmailResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/emails")
public class EmailController {

    private final TransactionalEmailService transactional;
    private final MarketingEmailService marketing;

    public EmailController(
        TransactionalEmailService transactional,
        MarketingEmailService marketing
    ) {
        this.transactional = transactional;
        this.marketing = marketing;
    }

    @PostMapping("/welcome")
    public CompletableFuture<ResponseEntity<SendEmailResponse>> sendWelcome(
        @Valid @RequestBody WelcomeRequest body
    ) {
        return transactional.sendWelcomeEmail(body.email(), body.firstName())
            .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/password-reset")
    public CompletableFuture<ResponseEntity<SendEmailResponse>> sendPasswordReset(
        @Valid @RequestBody PasswordResetRequest body
    ) {
        return transactional.sendPasswordResetEmail(body.email(), body.resetLink())
            .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/otp")
    public CompletableFuture<ResponseEntity<SendEmailResponse>> sendOtp(
        @Valid @RequestBody OtpRequest body
    ) {
        return transactional.sendOtpEmail(body.email(), body.otp())
            .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/order-confirmation")
    public CompletableFuture<ResponseEntity<SendEmailResponse>> sendOrderConfirmation(
        @Valid @RequestBody OrderConfirmationRequest body
    ) {
        return transactional.sendOrderConfirmation(body.email(), body.orderId(), body.total())
            .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/newsletter")
    public CompletableFuture<ResponseEntity<SendEmailResponse>> sendNewsletter(
        @Valid @RequestBody NewsletterRequest body
    ) {
        return marketing.sendNewsletter(body.recipients(), body.subject(), body.html())
            .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/newsletter/schedule")
    public CompletableFuture<ResponseEntity<SendEmailResponse>> scheduleNewsletter(
        @Valid @RequestBody ScheduleNewsletterRequest body
    ) {
        return marketing.scheduleNewsletter(
            body.recipients(), body.subject(), body.html(), body.scheduledFor()
        ).thenApply(ResponseEntity::ok);
    }

    record WelcomeRequest(
        @Email @NotBlank String email,
        @NotBlank String firstName
    ) {}

    record PasswordResetRequest(
        @Email @NotBlank String email,
        @NotBlank String resetLink
    ) {}

    record OtpRequest(
        @Email @NotBlank String email,
        @NotBlank String otp
    ) {}

    record OrderConfirmationRequest(
        @Email @NotBlank String email,
        @NotBlank String orderId,
        @NotBlank String total
    ) {}

    record NewsletterRequest(
        List<String> recipients,
        @NotBlank String subject,
        @NotBlank String html
    ) {}

    record ScheduleNewsletterRequest(
        List<String> recipients,
        @NotBlank String subject,
        @NotBlank String html,
        @NotBlank String scheduledFor
    ) {}
}
