package springcourse.solutions.exercise9.controller.exception;

/**
 * @author Amit Tal
 * @since 4/23/2014
 */
public class Error {

    private String message;
    private Code code;

    public enum Code {
        INVALID_INPUT,
        INVALID_STATE,
        DB_DOWN,
        UNKNOWN_ERROR
    }

    public Error() {
    }

    public Error(Code code, String message) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Error{");
        sb.append("message='").append(message).append('\'');
        sb.append(", code='").append(code).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
