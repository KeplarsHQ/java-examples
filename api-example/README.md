# Keplars Java API Example

Minimal Java program sending a single email via raw HTTP using Java's built-in `HttpClient` - no SDK required.

## Requirements

- JDK 17+
- Maven 3.8+
- A Keplars API key ([get one here](https://dash.keplars.com))

## Setup

```bash
cd api-example
cp .env.example .env
```

Set your credentials in `.env`:

```bash
API_KEY=kms_your_key_here
TO_EMAIL=recipient@example.com
FROM_EMAIL=hello@yourdomain.com
```

Run:

```bash
mvn compile exec:java -Dexec.mainClass=com.keplars.examples.Main
```

## License

MIT
