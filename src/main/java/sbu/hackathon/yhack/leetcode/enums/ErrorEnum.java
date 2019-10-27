package sbu.hackathon.yhack.leetcode.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum ErrorEnum {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User Not Found"),
    ;

    @Getter
    private final HttpStatus httpStatus;

    @Getter
    private final String message;

    ErrorEnum(HttpStatus httpStatus, String message) {

        this.httpStatus = httpStatus;
        this.message = message;
    }

}
