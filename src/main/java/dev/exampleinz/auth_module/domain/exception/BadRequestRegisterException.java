package dev.exampleinz.auth_module.domain.exception;

public class BadRequestRegisterException extends RuntimeException {
    public BadRequestRegisterException(String message) {
        super(message);
    }
}
