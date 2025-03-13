package org.example.app;

import org.example.infrastructure.annotation.Component;

@Component
public class DefaultEmailSender implements EmailSender {

    @Override
    public void send(String to, String subject, String body) {
        System.out.printf(
                "Sending email to %s. Subject: %s. Body: %s\n",
                to,
                subject,
                body
        );
    }
}

class Singleton extends DefaultEmailSender {}
class SingletonHolder extends DefaultEmailSender {}

