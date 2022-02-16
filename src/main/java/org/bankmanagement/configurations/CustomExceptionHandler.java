package org.bankmanagement.configurations;

import org.bankmanagement.data_transfer_objects.ErrorResponse;
import org.bankmanagement.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({UserAlreadyExistsByEmailException.class, UserAlreadyExistsByUsernameException.class})
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(RuntimeException e) {
        return new ResponseEntity(new ErrorResponse()
                .setCode("CONFLICT")
                .setStatus(409)
                .setMessage(e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UpdateRequestException.class)
    public ResponseEntity<ErrorResponse> handleUserUpdateRequestException(UpdateRequestException e) {
        return new ResponseEntity(new ErrorResponse()
                .setCode("UNPROCESSABLE_ENTITY")
                .setStatus(422)
                .setMessage(e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(UserIsDisabledException.class)
    public ResponseEntity<ErrorResponse> handleUserIsDisabledException(UserIsDisabledException e) {
        return new ResponseEntity(new ErrorResponse()
                .setCode("GONE")
                .setStatus(410)
                .setMessage(e.getMessage()), HttpStatus.GONE);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
        return new ResponseEntity(new ErrorResponse()
                .setCode("NOT_FOUND")
                .setStatus(404)
                .setMessage(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String message = "";
        for (FieldError error : fieldErrors) {
            message += error.getDefaultMessage() + "\n";
        }

        return new ResponseEntity(new ErrorResponse()
                .setCode("BAD_REQUEST")
                .setStatus(400)
                .setMessage(message), HttpStatus.BAD_REQUEST);
    }


}
