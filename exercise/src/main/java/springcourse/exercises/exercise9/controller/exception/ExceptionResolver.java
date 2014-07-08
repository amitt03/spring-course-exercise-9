package springcourse.exercises.exercise9.controller.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Amit Tal
 * @since 4/13/2014
 */
@ControllerAdvice
public class ExceptionResolver {

    private Logger logger = LoggerFactory.getLogger(ExceptionResolver.class);

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Error handleException(IllegalArgumentException e) {
        logger.info("Handling IllegalArgumentException", e);
        Error error = new Error(Error.Code.INVALID_INPUT, e.getMessage());
        return error;
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Error handleException(IllegalStateException e) {
        logger.info("Handling IllegalStateException", e);
        Error error = new Error(Error.Code.INVALID_STATE, e.getMessage());
        return error;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Error handleException(MethodArgumentNotValidException e) {
        logger.info("Handling MethodArgumentNotValidException", e);
        Error error = new Error(Error.Code.INVALID_INPUT, e.getMessage());
        return error;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Error handleException(Exception e) {
        logger.info("Handling Exception", e);
        Error error = new Error(Error.Code.UNKNOWN_ERROR, e.getMessage());
        return error;
    }
}
