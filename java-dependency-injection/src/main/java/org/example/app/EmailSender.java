package org.example.app;

public interface EmailSender {

    void send(String to, String subject, String body);
}
