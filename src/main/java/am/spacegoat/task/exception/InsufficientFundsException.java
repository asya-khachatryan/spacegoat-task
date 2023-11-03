package am.spacegoat.task.exception;

import lombok.Getter;

@Getter
public class InsufficientFundsException extends RuntimeException {
    private final String message;

    public InsufficientFundsException(String message) {
        super(message);
        this.message = message;
    }
}