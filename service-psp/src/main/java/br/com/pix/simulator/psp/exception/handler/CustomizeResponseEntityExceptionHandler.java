package br.com.pix.simulator.psp.exception.handler;

import br.com.pix.simulator.psp.config.LoggerConfig;
import br.com.pix.simulator.psp.dto.error.FieldError;
import br.com.pix.simulator.psp.dto.error.ResponseError;
import br.com.pix.simulator.psp.exception.ExceptionResponse;
import br.com.pix.simulator.psp.exception.InsufficientBalanceException;
import br.com.pix.simulator.psp.exception.ResourceNotFoundException;
import br.com.pix.simulator.psp.exception.ValidationException;
import lombok.Generated;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Generated
@RestControllerAdvice
public class CustomizeResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception exception, WebRequest request){
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(), exception.getMessage(), request.getDescription(false));
        LoggerConfig.LOGGER_EXCEPTION.error(exception.getMessage());
        return new  ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR); //500
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleNotFoundExceptions(Exception exception, WebRequest request){
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(), exception.getMessage(), request.getDescription(false));
        LoggerConfig.LOGGER_EXCEPTION.error(exception.getMessage());
        return new  ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND); //404
    }

    @ExceptionHandler(ValidationException.class)
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<FieldError> errors = exception.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new FieldError(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
        ResponseError responseError = new ResponseError(errors);
        LoggerConfig.LOGGER_EXCEPTION.error(exception.getMessage());
        return new ResponseEntity<>(responseError, HttpStatus.BAD_REQUEST); // 400
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public final ResponseEntity<ExceptionResponse> handleInsufficientBalance(Exception exception, WebRequest request){
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(), exception.getMessage(), request.getDescription(false));
        LoggerConfig.LOGGER_EXCEPTION.error(exception.getMessage());
        return new  ResponseEntity<>(exceptionResponse, HttpStatus.UNPROCESSABLE_ENTITY); //422
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        LoggerConfig.LOGGER_EXCEPTION.warn("Requisição inválida: {}", ex.getMessage());
        return new ResponseEntity<>(Map.of("erro", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
