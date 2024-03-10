package portefeuille.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import portefeuille.dto.ExceptionDto;
import portefeuille.exceptions.*;

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

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> notFoundException(NotFoundException ex, WebRequest request) {
        ExceptionDto dto = new ExceptionDto(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Non trouvé",
                request.getDescription(false).replaceFirst("uri=", "")
        );
        return handleExceptionInternal(ex, dto, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    protected ResponseEntity<Object> insufficientFundsException(InsufficientFundsException ex, WebRequest request) {
        ExceptionDto dto = new ExceptionDto(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Fonds insuffisants",
                request.getDescription(false).replaceFirst("uri=", "")
        );
        return handleExceptionInternal(ex, dto, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(NotEnoughStocksException.class)
    protected ResponseEntity<Object> notEnoughStocksException(NotEnoughStocksException ex, WebRequest request) {
        ExceptionDto dto = new ExceptionDto(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Pas assez d'actions possédées",
                request.getDescription(false).replaceFirst("uri=", "")
        );
        return handleExceptionInternal(ex, dto, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(WalletAlreadyCreatedException.class)
    protected ResponseEntity<Object> walletAlreadyCreatedException(WalletAlreadyCreatedException ex, WebRequest request) {
        ExceptionDto dto = new ExceptionDto(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Portefeuille déjà existant",
                request.getDescription(false).replaceFirst("uri=", "")
        );
        return handleExceptionInternal(ex, dto, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(TooManyFavorites.class)
    protected ResponseEntity<Object> tooManyFavorites(TooManyFavorites ex, WebRequest request) {
        ExceptionDto dto = new ExceptionDto(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                "Nombre maximum de favoris atteint",
                request.getDescription(false).replaceFirst("uri=", "")
        );
        return handleExceptionInternal(ex, dto, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }
}

