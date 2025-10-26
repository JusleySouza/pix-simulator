package br.com.pix.simulator.psp.exception;

import java.io.Serializable;
import java.util.Date;

import lombok.Generated;
import lombok.Getter;

@Generated
@Getter
public class ExceptionResponse implements Serializable{

    private static final long serialVersionUID = 1L;

    protected Date timestamp;
    protected String message;
    protected String details;

    public ExceptionResponse() {}

    public ExceptionResponse(Date timestamp, String message, String details) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

}
