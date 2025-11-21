package br.com.pix.simulator.dict.dto.error;

import jakarta.validation.ConstraintViolation;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Generated
public class ResponseError {

    private final String MESSAGE = "Validation Error";
    private Collection<FieldError> errors;

    public ResponseError(Collection<FieldError> errors) {
        this.errors = errors;
    }

    public static<T> ResponseError createFromValidations(Set<ConstraintViolation<T>> violations) {

        List<FieldError> errors = violations.stream()
                .map(violation -> new FieldError(violation.getPropertyPath().toString(), violation.getMessage()))
                .collect(Collectors.toList());

        ResponseError responseError = new ResponseError(errors);

        return responseError;
    }
}
