package com.gpb.exception;

import com.gpb.dto.ResponseDto;
import com.gpb.entity.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleAllExceptions(Exception ex, WebRequest request) {
        Error errorDetails = new Error(
                "Произошло что-то ужасное, но станет лучше, честно",
                "CreateAccountError",
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                UUID.randomUUID().toString()
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UserNotFoundException.class,})
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex) {
        Error error = new Error(
                ex.getMessage(),
                UserNotFoundException.class.getSimpleName(),
                HttpStatus.NOT_FOUND.toString(),
                UUID.randomUUID().toString()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UserAlreadyHasAccountException.class,})
    public ResponseEntity<?> UserAlreadyHasAccountException(UserAlreadyHasAccountException ex) {
        Error error = new Error(
                ex.getMessage(),
                UserAlreadyHasAccountException.class.getSimpleName(),
                HttpStatus.NOT_FOUND.toString(),
                UUID.randomUUID().toString()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({DatabaseConnectionFailureException.class,})
    public ResponseEntity<?> DatabaseConnectionFailureException(DatabaseConnectionFailureException ex) {
        Error error = new Error(
                ex.getMessage(),
                DatabaseConnectionFailureException.class.getSimpleName(),
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                UUID.randomUUID().toString()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({NotEnoughFundsException.class,})
    public ResponseEntity<?> NotEnoughFundsException(NotEnoughFundsException ex) {
        Error error = new Error(
                ex.getMessage(),
                NotEnoughFundsException.class.getSimpleName(),
                HttpStatus.BAD_REQUEST.toString(),
                UUID.randomUUID().toString()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AccountNotFoundException.class,})
    public ResponseEntity<?> AccountNotFoundException(AccountNotFoundException ex) {
        Error error = new Error(
                ex.getMessage(),
                AccountNotFoundException.class.getSimpleName(),
                HttpStatus.BAD_REQUEST.toString(),
                UUID.randomUUID().toString()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseDto> handleRuntimeException(RuntimeException ex) {
        ResponseDto response = new ResponseDto(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
