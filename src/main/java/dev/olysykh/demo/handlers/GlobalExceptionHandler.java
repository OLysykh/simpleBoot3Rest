package dev.olysykh.demo.handlers;

import dev.olysykh.demo.enums.BusinessErrorCodes;
import jakarta.mail.MessagingException;
import jakarta.mail.Service;
import java.util.HashSet;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ExceptionResponse> handledException(LockedException exception) {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(
                ExceptionResponse.builder()
                    .businessErrorCode(BusinessErrorCodes.ACCOUNT_LOCKED.getCode())
                    .businessErrorDescription(BusinessErrorCodes.ACCOUNT_LOCKED.getDescription())
                    .error(exception.getMessage())
                    .build()
            );
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionResponse> handledException(DisabledException exception) {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(
                ExceptionResponse.builder()
                    .businessErrorCode(BusinessErrorCodes.ACCOUNT_DISABLED.getCode())
                    .businessErrorDescription(BusinessErrorCodes.ACCOUNT_DISABLED.getDescription())
                    .error(exception.getMessage())
                    .build()
            );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handledException(BadCredentialsException exception) {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(
                ExceptionResponse.builder()
                    .businessErrorCode(BusinessErrorCodes.ACCOUNT_BAD_CREDENTIALS.getCode())
                    .businessErrorDescription(BusinessErrorCodes.ACCOUNT_BAD_CREDENTIALS.getDescription())
                    .error(exception.getMessage())
                    .build()
            );
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ExceptionResponse> handledException(MessagingException exception) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ExceptionResponse.builder()
                    .error(exception.getMessage())
                    .build()
            );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handledException(MethodArgumentNotValidException exception) {

        Set<String> errors = new HashSet<>();

        exception.getBindingResult().getAllErrors()
            .forEach(
                er -> {
                    var defaultError = er.getDefaultMessage();
                    errors.add(defaultError);
                });
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ExceptionResponse.builder()
                    .validationErrors(errors)
                    .build()
            );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handledException(Exception exception) {
        exception.printStackTrace();
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ExceptionResponse.builder()
                    .businessErrorDescription("Internal Error please contact tech support")
                    .error(exception.getMessage())
                    .build()
            );
    }

}
