package com.spacegoat.task.exception;

public class BalanceOperationException extends RuntimeException {
    private final String message;

    private final Object data;

    public BalanceOperationException(String message, Object data) {
        super(message);
        this.message = message;
        this.data = data;
    }

    public BalanceOperationException(String message) {
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
