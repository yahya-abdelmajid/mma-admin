package mma.admin.domains.exceptions;

public class NoPositiveCapacityException extends RuntimeException {
    public NoPositiveCapacityException(String message) {
        super(message);
    }
}
