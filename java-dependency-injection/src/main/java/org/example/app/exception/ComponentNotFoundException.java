package org.example.app.exception;

public class ComponentNotFoundException extends RuntimeException {
    public ComponentNotFoundException(String message) {
        super(message);
    }
}
