package hometask.errors;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.LinkedList;
import java.util.List;

@Schema(description = "Defines payload for errors.")
public class ErrorResponse {
    @Schema(example = "Invalid parameter")
    private String message;

    @Schema(example = "[\"Must be positive\", \"Must not exceed 10000\"]")
    private List<String> errors = new LinkedList<>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getErrors() {
        return errors;
    }
}
