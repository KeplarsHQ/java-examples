package com.keplars.examples.service;

import com.keplars.KeplarsClient;
import com.keplars.models.ScheduleEmailRequest;
import com.keplars.models.SendEmailRequest;
import com.keplars.models.SendEmailResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class MarketingEmailService {

    private final KeplarsClient keplars;

    public MarketingEmailService(KeplarsClient keplars) {
        this.keplars = keplars;
    }

    public CompletableFuture<SendEmailResponse> sendNewsletter(
        List<String> recipients,
        String subject,
        String htmlContent
    ) {
        SendEmailRequest request = new SendEmailRequest(
            recipients,
            "newsletter@yourdomain.com",
            subject,
            htmlContent,
            null, null, null, null, null, null, null
        );
        return keplars.getEmails().sendBulkAsync(request);
    }

    public CompletableFuture<SendEmailResponse> sendCampaignWithTemplate(
        List<String> recipients,
        String templateId,
        Map<String, Object> templateData
    ) {
        SendEmailRequest request = new SendEmailRequest(
            recipients,
            "campaigns@yourdomain.com",
            "Check out what's new",
            null, null,
            templateId,
            templateData,
            null, null, null, null
        );
        return keplars.getEmails().sendBulkAsync(request);
    }

    public CompletableFuture<SendEmailResponse> scheduleNewsletter(
        List<String> recipients,
        String subject,
        String htmlContent,
        String scheduledFor
    ) {
        ScheduleEmailRequest request = new ScheduleEmailRequest(
            recipients,
            "newsletter@yourdomain.com",
            subject,
            htmlContent,
            null, null, null, null, null, null, null,
            scheduledFor,
            "UTC",
            "bulk"
        );
        return keplars.getEmails().scheduleAsync(request);
    }
}
