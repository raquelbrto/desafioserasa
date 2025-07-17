package com.dev.brito.desafioserasa.config;

import com.dev.brito.desafioserasa.dto.ApiResponseErrorDTO;
import com.dev.brito.desafioserasa.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(PersonNotFoundException.class)
    private ResponseEntity<ApiResponseErrorDTO> personNotFoundHandler(PersonNotFoundException exception) {
        ApiResponseErrorDTO responseErrorDTO = new ApiResponseErrorDTO(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseErrorDTO);
    }

    @ExceptionHandler(InvalidScoreException.class)
    private ResponseEntity<ApiResponseErrorDTO> personNotFoundHandler(InvalidScoreException exception) {
        ApiResponseErrorDTO responseErrorDTO = new ApiResponseErrorDTO(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseErrorDTO);
    }

    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<ApiResponseErrorDTO> handleAddressNotFound(AddressNotFoundException exception) {
        ApiResponseErrorDTO responseErrorDTO = new ApiResponseErrorDTO(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseErrorDTO);
    }

    @ExceptionHandler(PersonAlreadyActiveException.class)
    public ResponseEntity<ApiResponseErrorDTO> handlePersonAlreadyActive(PersonAlreadyActiveException ex) {
        ApiResponseErrorDTO responseErrorDTO = new ApiResponseErrorDTO(HttpStatus.CONFLICT, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseErrorDTO);
    }

    @ExceptionHandler(PersonAlreadyInactiveException.class)
    public ResponseEntity<ApiResponseErrorDTO> handlePersonAlreadyActive(PersonAlreadyInactiveException ex) {
        ApiResponseErrorDTO responseErrorDTO = new ApiResponseErrorDTO(HttpStatus.CONFLICT, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseErrorDTO);
    }

    @ExceptionHandler(InvalidPersonFilterException.class)
    public ResponseEntity<ApiResponseErrorDTO> handleAddressNotFound(InvalidPersonFilterException exception) {
        ApiResponseErrorDTO responseErrorDTO = new ApiResponseErrorDTO(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseErrorDTO);
    }
}
