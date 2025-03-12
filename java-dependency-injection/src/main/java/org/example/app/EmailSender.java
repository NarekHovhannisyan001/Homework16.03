package org.example.app;

import org.example.infrastructure.annotation.Component;


public interface EmailSender {

    void send(String to, String subject, String body);
}
