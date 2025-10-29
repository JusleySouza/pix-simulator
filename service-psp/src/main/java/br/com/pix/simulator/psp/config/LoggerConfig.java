package br.com.pix.simulator.psp.config;

import br.com.pix.simulator.psp.exception.handler.CustomizeResponseEntityExceptionHandler;
import br.com.pix.simulator.psp.service.AccountService;
import br.com.pix.simulator.psp.service.PspService;
import br.com.pix.simulator.psp.service.UserService;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Generated
public class LoggerConfig {

    public static final Logger LOGGER_ACCOUNT = LoggerFactory.getLogger(AccountService.class);
    public static final Logger LOGGER_PSP = LoggerFactory.getLogger(PspService.class);
    public static final Logger LOGGER_USER = LoggerFactory.getLogger(UserService.class);
    public static final Logger LOGGER_EXCEPTION = LoggerFactory.getLogger(CustomizeResponseEntityExceptionHandler.class);

}
