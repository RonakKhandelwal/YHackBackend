
package sbu.hackathon.yhack.leetcode.exceptions;

import lombok.Getter;
import sbu.hackathon.yhack.leetcode.enums.ErrorEnum;

@Getter
public class ServiceException extends YHackException {

    private final String message;
    private final Integer errorCode;

    public ServiceException(Integer errorCode, String message) {
        super(message);
        this.message = message;
        this.errorCode = errorCode;
    }

    public ServiceException(Integer errorCode, String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.errorCode = errorCode;
    }

    public ServiceException(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
        this.message = errorEnum.getMessage();
        this.errorCode = errorEnum.getHttpStatus().value();
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
