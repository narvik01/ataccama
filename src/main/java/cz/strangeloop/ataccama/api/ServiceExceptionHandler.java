package cz.strangeloop.ataccama.api;

import cz.strangeloop.ataccama.api.dto.ErrorDto;
import cz.strangeloop.ataccama.service.CredentialsException;
import cz.strangeloop.ataccama.service.NotFoundException;
import cz.strangeloop.ataccama.service.UnknownDBException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ServiceExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    public ErrorDto notFound(NotFoundException e) {
        return new ErrorDto(e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(UnknownDBException.class)
    @ResponseBody
    public ErrorDto dbError(UnknownDBException e) {
        return new ErrorDto("Unknown error.");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(CredentialsException.class)
    @ResponseBody
    public ErrorDto credentials(CredentialsException e) {
        return new ErrorDto(e.getMessage());
    }
}
