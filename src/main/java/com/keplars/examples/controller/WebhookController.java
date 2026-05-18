package com.keplars.examples.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keplars.examples.service.WebhookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/webhooks")
public class WebhookController {

    private final WebhookService webhookService;
    private final ObjectMapper objectMapper;

    public WebhookController(WebhookService webhookService, ObjectMapper objectMapper) {
        this.webhookService = webhookService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/keplars")
    public ResponseEntity<Void> handleWebhook(
        @RequestHeader(value = "X-Keplars-Signature", required = false) String signature,
        @RequestBody String rawPayload
    ) {
        if (!webhookService.verifySignature(rawPayload, signature)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            Map<String, Object> event = objectMapper.readValue(
                rawPayload, new TypeReference<>() {}
            );
            webhookService.handleEvent(event);
        } catch (Exception ignored) {}
        return ResponseEntity.ok().build();
    }
}
