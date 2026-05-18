package com.keplars.examples;

import com.keplars.KeplarsClient;
import com.keplars.models.SendEmailData;
import com.keplars.models.SendEmailResponse;
import com.keplars.examples.service.TransactionalEmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KeplarsClient keplarsClient;

    @MockBean
    private TransactionalEmailService transactionalEmailService;

    private final SendEmailResponse mockResponse = new SendEmailResponse(
        true,
        "Email queued",
        new SendEmailData("job_test_123", "instant")
    );

    @Test
    void sendWelcomeEmail_returns200() throws Exception {
        when(transactionalEmailService.sendWelcomeEmail(any(), any()))
            .thenReturn(CompletableFuture.completedFuture(mockResponse));

        mockMvc.perform(post("/api/emails/welcome")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"user@example.com\",\"firstName\":\"Jane\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.jobId").value("job_test_123"));
    }

    @Test
    void sendWelcomeEmail_invalidEmail_returns400() throws Exception {
        mockMvc.perform(post("/api/emails/welcome")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"not-an-email\",\"firstName\":\"Jane\"}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void sendOtp_returns200() throws Exception {
        when(transactionalEmailService.sendOtpEmail(any(), any()))
            .thenReturn(CompletableFuture.completedFuture(mockResponse));

        mockMvc.perform(post("/api/emails/otp")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"user@example.com\",\"otp\":\"123456\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.priority").value("instant"));
    }

    @Test
    void sendPasswordReset_returns200() throws Exception {
        when(transactionalEmailService.sendPasswordResetEmail(any(), any()))
            .thenReturn(CompletableFuture.completedFuture(mockResponse));

        mockMvc.perform(post("/api/emails/password-reset")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"user@example.com\",\"resetLink\":\"https://example.com/reset/abc\"}"))
            .andExpect(status().isOk());
    }
}
