package br.com.pix.simulator.dict.service;

import br.com.pix.simulator.dict.config.LoggerConfig;
import br.com.pix.simulator.dict.dto.KeyCreateRequest;
import br.com.pix.simulator.dict.dto.PspAccountValidationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class PspClientService {

    private final RestTemplate restTemplate;

    @Value("${psp.service.url}")
    private String pspServiceUrl;

    public PspClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean validateAccount(KeyCreateRequest request) {
        String url = String.format(
                "%s/%s/valid?userId=%s&pspId=%s",
                pspServiceUrl,
                request.accountId(),
                request.userId(),
                request.pspId()
        );

        try {
            LoggerConfig.LOGGER_PSP_CLIENT.info("Validating account on PSP: {}", url);
            PspAccountValidationResponse response = restTemplate.getForObject(url, PspAccountValidationResponse.class);

            if (response == null) {
                LoggerConfig.LOGGER_PSP_CLIENT.warn("Account validation failed (invalid response from PSP).");
                return false;
            }

            LoggerConfig.LOGGER_PSP_CLIENT.info("Account validation OK. User: {}", response.userName());
            return true;

        } catch (HttpClientErrorException.NotFound e) {
            LoggerConfig.LOGGER_PSP_CLIENT.warn("Account validation failed. Reason: Account/User/PSP not found in service-psp.");
            return false;
        } catch (Exception e) {
            LoggerConfig.LOGGER_PSP_CLIENT.error("Error while trying to validate account on service-psp.", e);
            return false;
        }
    }
}
