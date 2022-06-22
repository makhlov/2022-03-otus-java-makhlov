package ru.otus.processor.exception;

public class EvenSecondException extends RuntimeException {
    public EvenSecondException() {
        super("This second is even");
    }

    public EvenSecondException(String message) {
        super(message);
    }

    public EvenSecondException(String message, Throwable cause) {
        super(message, cause);
    }
}
