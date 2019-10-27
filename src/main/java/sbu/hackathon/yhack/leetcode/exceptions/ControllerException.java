
package sbu.hackathon.yhack.leetcode.exceptions;

import lombok.Getter;
import org.springframework.validation.Errors;

@Getter
public class ControllerException extends YHackException {

    private final String message;
    private final Integer responseCode;
    private final transient Errors bindingErrors;

    public ControllerException(Integer responseCode, String message) {
        super(message);
        this.responseCode = responseCode;
        this.message = message;
        this.bindingErrors = null;
    }

    public ControllerException(Errors bindingErrors, String message) {
        super(message);
        this.responseCode = 400;
        this.bindingErrors = bindingErrors;
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
