package br.com.pix.simulator.dict.config;

import br.com.pix.simulator.dict.exception.handler.CustomizeResponseEntityExceptionHandler;
import br.com.pix.simulator.dict.service.PixKeyService;
import br.com.pix.simulator.dict.service.PspClientService;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Generated
public class LoggerConfig {

    public static final Logger LOGGER_PIX_KEY = LoggerFactory.getLogger(PixKeyService.class);
    public static final Logger LOGGER_PSP_CLIENT = LoggerFactory.getLogger(PspClientService.class);
    public static final Logger LOGGER_EXCEPTION = LoggerFactory.getLogger(CustomizeResponseEntityExceptionHandler.class);

}
