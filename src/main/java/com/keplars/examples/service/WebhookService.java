package com.keplars.examples.service;

import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Map;

@Service
public class WebhookService {

    private final String webhookSecret;

    public WebhookService() {
        this.webhookSecret = System.getenv("KEPLARS_WEBHOOK_SECRET");
    }

    public boolean verifySignature(String payload, String signature) {
        if (webhookSecret == null || signature == null) return false;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(webhookSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            String expected = "sha256=" + HexFormat.of().formatHex(
                mac.doFinal(payload.getBytes(StandardCharsets.UTF_8))
            );
            return expected.equals(signature);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            return false;
        }
    }

    public void handleEvent(Map<String, Object> event) {
        String type = (String) event.get("type");
        if (type == null) return;

        switch (type) {
            case "email.delivered" -> handleDelivered(event);
            case "email.bounced" -> handleBounced(event);
            case "email.complained" -> handleComplained(event);
            case "email.opened" -> handleOpened(event);
            case "email.clicked" -> handleClicked(event);
            case "email.failed" -> handleFailed(event);
        }
    }

    private void handleDelivered(Map<String, Object> event) {
        System.out.printf("Email delivered: %s%n", event.get("email_id"));
    }

    private void handleBounced(Map<String, Object> event) {
        System.out.printf("Email bounced: %s — reason: %s%n", event.get("email_id"), event.get("reason"));
    }

    private void handleComplained(Map<String, Object> event) {
        System.out.printf("Spam complaint: %s%n", event.get("recipient"));
    }

    private void handleOpened(Map<String, Object> event) {
        System.out.printf("Email opened: %s by %s%n", event.get("email_id"), event.get("recipient"));
    }

    private void handleClicked(Map<String, Object> event) {
        System.out.printf("Link clicked: %s in email %s%n", event.get("url"), event.get("email_id"));
    }

    private void handleFailed(Map<String, Object> event) {
        System.out.printf("Email failed: %s — %s%n", event.get("email_id"), event.get("error"));
    }
}
