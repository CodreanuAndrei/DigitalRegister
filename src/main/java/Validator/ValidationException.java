package Validator;

public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
