
package sbu.hackathon.yhack.leetcode.exceptions;

public class YHackException extends RuntimeException {

    private final String message;

    public YHackException(String message) {
        super(message, null, false, false);
        this.message = message;
    }

    public YHackException(String message, Throwable cause) {
        super(message, cause, false, false);
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
