package bourse.config;

import bourse.dto.ExceptionDto;
import bourse.exceptions.UnauthorizedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ErrorExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> exception(Exception ex, WebRequest request) {
        ExceptionDto dto = new ExceptionDto(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Une erreur est survenue",
                request.getDescription(false).replaceFirst("uri=", "")
        );
        return handleExceptionInternal(ex, dto, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(UnauthorizedException.class)
    protected ResponseEntity<Object> UnauthorizedException(UnauthorizedException ex, WebRequest request) {
        ExceptionDto dto = new ExceptionDto(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                request.getDescription(false).replaceFirst("uri=", "")
        );
        return handleExceptionInternal(ex, dto, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }
}
