package com.spacegoat.task.exception;

public class InsufficientFundsException extends RuntimeException {
    private final String message;

    private final Object data;

    public InsufficientFundsException(String message, Object data) {
        super(message);
        this.message = message;
        this.data = data;
    }

    public InsufficientFundsException(String message) {
        super(message);
        this.message = message;
        this.data = null;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}