package com.keplars.examples.service;

import com.keplars.KeplarsClient;
import com.keplars.models.SendEmailRequest;
import com.keplars.models.SendEmailResponse;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class TransactionalEmailService {

    private final KeplarsClient keplars;

    public TransactionalEmailService(KeplarsClient keplars) {
        this.keplars = keplars;
    }

    public CompletableFuture<SendEmailResponse> sendWelcomeEmail(String toEmail, String firstName) {
        SendEmailRequest request = new SendEmailRequest(
            toEmail,
            "welcome@yourdomain.com",
            "Welcome to Our Platform!",
            "<h1>Welcome, " + firstName + "!</h1><p>We're glad to have you on board.</p>",
            "Welcome, " + firstName + "! We're glad to have you on board.",
            null, null, null, null, null, null
        );
        return keplars.getEmails().sendInstantAsync(request);
    }

    public CompletableFuture<SendEmailResponse> sendWelcomeEmailWithTemplate(
        String toEmail,
        String firstName
    ) {
        SendEmailRequest request = new SendEmailRequest(
            toEmail,
            "welcome@yourdomain.com",
            "Welcome to Our Platform!",
            null, null,
            "tmpl_welcome_001",
            Map.of("first_name", firstName, "dashboard_url", "https://app.yourdomain.com"),
            null, null, null, null
        );
        return keplars.getEmails().sendInstantAsync(request);
    }

    public CompletableFuture<SendEmailResponse> sendPasswordResetEmail(
        String toEmail,
        String resetLink
    ) {
        SendEmailRequest request = new SendEmailRequest(
            toEmail,
            "security@yourdomain.com",
            "Reset Your Password",
            "<p>Click <a href=\"" + resetLink + "\">here</a> to reset your password. Link expires in 15 minutes.</p>",
            "Visit this link to reset your password: " + resetLink + " (expires in 15 minutes)",
            null, null, null, null, null, null
        );
        return keplars.getEmails().sendHighAsync(request);
    }

    public CompletableFuture<SendEmailResponse> sendOtpEmail(String toEmail, String otp) {
        SendEmailRequest request = new SendEmailRequest(
            toEmail,
            "security@yourdomain.com",
            "Your Verification Code: " + otp,
            "<h2>Your code is: <strong>" + otp + "</strong></h2><p>This code expires in 10 minutes.</p>",
            "Your verification code is: " + otp + " (expires in 10 minutes)",
            null, null, null, null, null, null
        );
        return keplars.getEmails().sendInstantAsync(request);
    }

    public CompletableFuture<SendEmailResponse> sendOrderConfirmation(
        String toEmail,
        String orderId,
        String total
    ) {
        SendEmailRequest request = new SendEmailRequest(
            toEmail,
            "orders@yourdomain.com",
            "Order Confirmed – #" + orderId,
            null, null,
            "tmpl_order_confirmation",
            Map.of("order_id", orderId, "total", total),
            null, null, null, null
        );
        return keplars.getEmails().sendHighAsync(request);
    }
}
