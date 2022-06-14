package ru.otus.atm.interaction.exception;

public class AtmInteractionException extends Exception {
    public AtmInteractionException() {
    }

    public AtmInteractionException(String message) {
        super(message);
    }

    public AtmInteractionException(String message, Throwable cause) {
        super(message, cause);
    }
}
