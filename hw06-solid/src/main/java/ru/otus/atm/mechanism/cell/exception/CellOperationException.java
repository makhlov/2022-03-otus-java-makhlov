package ru.otus.atm.mechanism.cell.exception;

public class CellOperationException extends Exception {
    public CellOperationException(String message) {
        super(message);
    }

    public CellOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}