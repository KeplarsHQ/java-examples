package com.keplars.examples;

import io.github.cdimascio.dotenv.Dotenv;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    public static void main(String[] args) throws Exception {
        Dotenv env = Dotenv.configure().ignoreIfMissing().load();

        String apiBaseUrl = env.get("API_BASE_URL", "https://api.keplars.com/api/v1");
        String apiKey = env.get("API_KEY");
        String toEmail = env.get("TO_EMAIL");
        String fromEmail = env.get("FROM_EMAIL", "hello@yourdomain.com");
        String userName = env.get("USER_NAME", "there");

        String payload = """
                {
                    "to": ["%s"],
                    "from": "%s",
                    "subject": "Welcome %s!",
                    "body": "<h1>Welcome %s!</h1><p>Thank you for joining our platform.</p>",
                    "is_html": true
                }
                """.formatted(toEmail, fromEmail, userName, userName);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiBaseUrl + "/send-email/instant"))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            System.out.println("Email sent successfully!");
            System.out.println("Response: " + response.body());
        } else {
            System.err.println("Error sending email (status " + response.statusCode() + "): " + response.body());
            System.exit(1);
        }
    }
}
