# Keplars Java Examples

Spring Boot demo project showing how to integrate the [Keplars](https://keplars.com) email SDK into a Java application.

## Requirements

- Java 17+
- Maven 3.8+
- A Keplars API key ([get one here](https://dash.keplars.com))

## SDK Dependency

Add to your `pom.xml`:

```xml
<dependency>
    <groupId>com.keplars</groupId>
    <artifactId>keplars-kotlin</artifactId>
    <version>1.10.5</version>
</dependency>
```

Or `build.gradle`:

```groovy
implementation 'com.keplars:keplars-kotlin:1.10.5'
```

Then import and initialise:

```java
import com.keplars.KeplarsClient;
import com.keplars.models.SendEmailRequest;
import com.keplars.models.SendEmailResponse;

KeplarsClient client = KeplarsClient.create("kms_your_api_key");

SendEmailResponse response = client.getEmails().sendInstantAsync(
    new SendEmailRequest(
        "user@example.com",      // to
        "hello@yourdomain.com",  // from
        "Hello!",                // subject
        "<h1>Hello World</h1>",  // html
        null, null, null, null, null, null, null
    )
).get();
```

## Setup

```bash
git clone git@github.com:KeplarsHQ/java-examples.git
cd java-examples
```

Set your API key:

```bash
export KEPLARS_API_KEY=kms_your_key_here
```

Run the application:

```bash
./mvnw spring-boot:run
```

The server starts on `http://localhost:8080`.

## Project Structure

```
src/main/java/com/keplars/examples/
├── KeplarsExamplesApplication.java      Entry point
├── config/
│   └── KeplarsConfig.java               KeplarsClient Spring bean
├── controller/
│   ├── EmailController.java             REST endpoints
│   └── WebhookController.java           Webhook receiver
└── service/
    ├── TransactionalEmailService.java   Welcome, OTP, password reset, orders
    ├── MarketingEmailService.java       Newsletters, campaigns, scheduling
    └── WebhookService.java              Signature verification + event handling
```

## API Endpoints

### Transactional Emails

**Send welcome email:**
```bash
curl -X POST http://localhost:8080/api/emails/welcome \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","firstName":"Jane"}'
```

**Send OTP:**
```bash
curl -X POST http://localhost:8080/api/emails/otp \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","otp":"847291"}'
```

**Send password reset:**
```bash
curl -X POST http://localhost:8080/api/emails/password-reset \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","resetLink":"https://yourapp.com/reset/abc123"}'
```

**Send order confirmation:**
```bash
curl -X POST http://localhost:8080/api/emails/order-confirmation \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","orderId":"ORD-9921","total":"$49.99"}'
```

### Marketing Emails

**Send newsletter (bulk):**
```bash
curl -X POST http://localhost:8080/api/emails/newsletter \
  -H "Content-Type: application/json" \
  -d '{
    "recipients": ["a@example.com","b@example.com"],
    "subject": "Our Monthly Update",
    "html": "<h1>Hello!</h1><p>Here is what is new this month.</p>"
  }'
```

**Schedule newsletter:**
```bash
curl -X POST http://localhost:8080/api/emails/newsletter/schedule \
  -H "Content-Type: application/json" \
  -d '{
    "recipients": ["a@example.com","b@example.com"],
    "subject": "Weekend Deal",
    "html": "<h1>Special offer inside!</h1>",
    "scheduledFor": "2025-06-01T09:00:00Z"
  }'
```

### Webhooks

Point your Keplars webhook URL to:
```
POST https://your-server.com/webhooks/keplars
```

Set the webhook secret:
```bash
export KEPLARS_WEBHOOK_SECRET=your_webhook_secret
```

Supported events: `email.delivered`, `email.bounced`, `email.complained`, `email.opened`, `email.clicked`, `email.failed`

## Priority Queue

The SDK uses Keplars' 4-tier priority system automatically:

| Use Case | Method | Priority |
|---|---|---|
| OTP, auth codes | `sendInstantAsync` | Instant (fastest) |
| Password reset, alerts | `sendHighAsync` | High |
| Welcome, notifications | `sendJava` | Async |
| Newsletters, campaigns | `sendBulkAsync` | Bulk |

## Running Tests

```bash
./mvnw test
```

## SDK Dependency

This project uses the official Keplars Kotlin SDK which works seamlessly from Java:

```xml
<dependency>
    <groupId>com.keplars</groupId>
    <artifactId>keplars-kotlin</artifactId>
    <version>1.10.5</version>
</dependency>
```

All async operations return `CompletableFuture<T>`, integrating naturally with Spring's async support.

## Related Examples

- [Kotlin Examples](https://github.com/KeplarsHQ/kotlin-examples) — Ktor + coroutines, idiomatic `suspend` API

## License

MIT
