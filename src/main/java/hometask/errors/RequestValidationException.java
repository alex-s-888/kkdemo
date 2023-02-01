package hometask.errors;

import java.util.List;

public class RequestValidationException extends RuntimeException {

    private List<String> errors;

    public RequestValidationException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
