package am.spacegoat.task.exception;

public class BalanceOperationException extends RuntimeException {
    private final String message;

    public BalanceOperationException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
