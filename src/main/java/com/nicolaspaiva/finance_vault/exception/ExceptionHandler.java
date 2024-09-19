package com.nicolaspaiva.finance_vault.exception;

import com.nicolaspaiva.finance_vault.auth.dto.SignUpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandler {


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleInvalidArgument(MethodArgumentNotValidException ex){

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {

            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);

        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


    /**
     * Handles authentication exceptions,
     * such as wrong password or email
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleAuthenticationException(AuthenticationException ex) {

        Map<String, String> authenticationError = new HashMap<>();
        authenticationError.put("error", "Invalid username or password");

        return new ResponseEntity<>(authenticationError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles exceptions when the
     * user is not found
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }


    // EXAMPLE OF CUSTOM EXCEPTION
    /**
     * Handles the exception when the
     * user tries to create an account
     * with an already existing email
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @org.springframework.web.bind.annotation.ExceptionHandler(UserSignUpException.class)
    public SignUpResponse handleEmailAlreadyExistsException(UserSignUpException exception){
        return new SignUpResponse(false, exception.getMessage());
    }

}
