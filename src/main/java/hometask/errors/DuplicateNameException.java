package hometask.errors;

import java.util.LinkedList;
import java.util.List;

public class DuplicateNameException extends RuntimeException {

    private List<String> errors = new LinkedList<>();

    public DuplicateNameException(String message, String error) {
        super(message);
        this.errors.add(error);
    }

    public List<String> getErrors() {
        return errors;
    }
}
