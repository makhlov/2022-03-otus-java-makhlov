package ru.otus.atm.mechanism.cluster.exception;

public class ClusterOperationException extends Exception {
    public ClusterOperationException() {
    }

    public ClusterOperationException(String message) {
        super(message);
    }

    public ClusterOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
