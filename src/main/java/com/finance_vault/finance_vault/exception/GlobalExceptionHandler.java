package com.finance_vault.finance_vault.exception;

import com.finance_vault.finance_vault.dto.error.ErrorResponse;
import com.finance_vault.finance_vault.dto.auth.RegistrationResponse;
import com.finance_vault.finance_vault.dto.auth.LoginErrorResponse;
import com.finance_vault.finance_vault.dto.auth.LoginResponse;
import com.finance_vault.finance_vault.dto.profile.ProfileDataChangeResponse;
import com.finance_vault.finance_vault.dto.transaction.TransactionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Returns an error response when
     * validation at the controller fails
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        ErrorResponse response = ErrorResponse.builder()
                .message("Validation failed")
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .errors(errors)
                .build();

        return ResponseEntity.badRequest().body(response);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleInvalidArguments(IllegalArgumentException ex) {

        ErrorResponse response = ErrorResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(response);
    }


    /**
     * Handles the exception when the user provides an already existing username,
     * or when the password does not match specified conditions.
     */
    @ExceptionHandler(InvalidRegistrationException.class)
    public ResponseEntity<RegistrationResponse> handleInvalidEmail(InvalidRegistrationException e) {
        return ResponseEntity.badRequest().body(RegistrationResponse.failed(e.getMessage()));
    }


    /**
     * Handles the case where the user tries to log in
     * but there is no user or the credentials are incorrect
     */
    @ExceptionHandler(InvalidEmailOrPasswordException.class)
    public ResponseEntity<LoginResponse> handleInvalidEmailOrPassword() {
        return ResponseEntity.badRequest().body(new LoginErrorResponse());
    }


    /**
     * Handles the case where the user does not exist
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFound(UserNotFoundException e) {
        ErrorResponse errorBody = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
        return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
    }


    /**
     * handles the case where the transaction is invalid
     */
    @ExceptionHandler(InvalidTransactionException.class)
    public ResponseEntity<TransactionResponse> handleInvalidTransaction(InvalidTransactionException e) {
        return ResponseEntity.badRequest().body(TransactionResponse.failed(e.getMessage()));
    }


    /**
     * Handles exceptions during profile changes
     */
    @ExceptionHandler(InvalidProfileChangeException.class)
    public ResponseEntity<ProfileDataChangeResponse> handleInvalidDataChange(InvalidProfileChangeException e) {
        return ResponseEntity.badRequest().body(new ProfileDataChangeResponse(false, e.getMessage(), null));
    }
}
