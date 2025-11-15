package br.com.pix.simulator.dict.dto;

public record PspAccountValidationResponse(
        boolean isValid,
        String userName,
        String pspName
) { }
