# Keplars Java Examples

Example projects showing how to send email with [Keplars](https://keplars.com) from Java.

## Examples

| Directory | Description |
|---|---|
| [`api-example`](./api-example) | Raw HTTP - no SDK, uses Java's built-in `HttpClient` |
| [`sdk-example`](./sdk-example) | Full Spring Boot server using the Keplars SDK |
| [`smtp-example`](./smtp-example) | SMTP relay via Jakarta Mail |

## SDK Install

Add to `pom.xml`:

```xml
<dependency>
    <groupId>com.keplars</groupId>
    <artifactId>keplars-kotlin</artifactId>
    <version>1.10.5</version>
</dependency>
```

## Quick Start

```java
import com.keplars.KeplarsClient;
import com.keplars.models.SendEmailRequest;
import com.keplars.models.SendEmailResponse;

KeplarsClient client = KeplarsClient.create("kms_your_api_key");

SendEmailResponse response = client.getEmails().sendInstantAsync(
    new SendEmailRequest.Builder()
        .to("user@example.com")
        .from("hello@yourdomain.com")
        .subject("Hello!")
        .body("<h1>Hello World</h1>")
        .isHtml(true)
        .build()
).get();
```

## Related

- [Kotlin Examples](https://github.com/KeplarsHQ/kotlin-examples) - Ktor + coroutines

## License

MIT
