package dev.exampleinz.auth_module.domain.exception;

public class BadRequestLoginException extends RuntimeException {
    public BadRequestLoginException(String message) {
        super(message);
    }
}
