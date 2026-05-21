package com.keplars.examples;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws Exception {
        Dotenv env = Dotenv.configure().ignoreIfMissing().load();

        String smtpHost = env.get("SMTP_HOST", "smtp.keplars.com");
        String smtpPort = env.get("SMTP_PORT", "587");
        String username = env.get("KEPLARS_SMTP_USERNAME");
        String password = env.get("KEPLARS_SMTP_PASSWORD");
        String fromEmail = env.get("FROM_EMAIL", "hello@yourdomain.com");
        String toEmail = env.get("TO_EMAIL");
        String userName = env.get("USER_NAME", "there");

        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("Welcome " + userName + "!");
        message.setContent(
            "<h1>Welcome " + userName + "!</h1><p>Thank you for joining our platform.</p>",
            "text/html; charset=utf-8"
        );

        Transport.send(message);
        System.out.println("Email sent successfully to " + toEmail);
    }
}
