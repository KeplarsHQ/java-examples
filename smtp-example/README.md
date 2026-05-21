# Keplars Java SMTP Example

Send email via Keplars SMTP relay using Jakarta Mail - no SDK required.

## Requirements

- JDK 17+
- Maven 3.8+
- A Keplars API key ([get one here](https://dash.keplars.com))

## Setup

```bash
cd smtp-example
cp .env.example .env
```

Set your credentials in `.env`:

```bash
SMTP_HOST=smtp.keplars.com
SMTP_PORT=587
KEPLARS_SMTP_USERNAME=kms_your_key_here
KEPLARS_SMTP_PASSWORD=kms_your_key_here
FROM_EMAIL=hello@yourdomain.com
TO_EMAIL=recipient@example.com
```

Run:

```bash
mvn compile exec:java -Dexec.mainClass=com.keplars.examples.Main
```

## License

MIT
