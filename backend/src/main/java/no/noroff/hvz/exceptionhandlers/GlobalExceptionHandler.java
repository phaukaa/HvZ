package no.noroff.hvz.exceptionhandlers;

import no.noroff.hvz.exceptions.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException e, WebRequest r) {
        return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException e, WebRequest r) {
        return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNoSuchElementException(NullPointerException e, WebRequest r) {
        return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidBiteCodeException.class)
    public ResponseEntity<String> handleInvalidBiteCodeException(InvalidBiteCodeException e, WebRequest r) {
        return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AppUserNotFoundException.class)
    public ResponseEntity<String> handleInvalidBiteCodeException(AppUserNotFoundException e, WebRequest r) {
        return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingPermissionsException.class)
    public ResponseEntity<String> handleInvalidBiteCodeException(MissingPermissionsException e, WebRequest r) {
        return new ResponseEntity<>(e.getMessage(),HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AppUserAlreadyExistException.class)
    public ResponseEntity<String> handleInvalidBiteCodeException(AppUserAlreadyExistException e, WebRequest r) {
        return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MissingPlayerException.class)
    public ResponseEntity<String> handleInvalidBiteCodeException(MissingPlayerException e, WebRequest r) {
        return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PlayerAlreadyExistException.class)
    public ResponseEntity<String> handleInvalidBiteCodeException(PlayerAlreadyExistException e, WebRequest r) {
        return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
    }
}
