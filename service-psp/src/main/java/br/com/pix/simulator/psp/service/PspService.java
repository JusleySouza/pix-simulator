package br.com.pix.simulator.psp.service;

import br.com.pix.simulator.psp.config.LoggerConfig;
import br.com.pix.simulator.psp.dto.psps.PspCreateRequest;
import br.com.pix.simulator.psp.dto.psps.PspResponse;
import br.com.pix.simulator.psp.exception.ResourceNotFoundException;
import br.com.pix.simulator.psp.mapper.PspMapper;
import br.com.pix.simulator.psp.model.Psp;
import br.com.pix.simulator.psp.repository.PspRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PspService {

    private Psp psp;
    private final PspMapper mapper;
    private final PspRepository pspRepository;

    public PspService(PspMapper mapper, PspRepository pspRepository) {
        this.mapper = mapper;
        this.pspRepository = pspRepository;
    }

    @Transactional
    public PspResponse createPsp(PspCreateRequest request) {

        if(pspRepository.findByBankCode(request.bankCode()).isPresent()){
            throw new IllegalArgumentException("There is already a PSP with the bank code: " + request.bankCode());
        }

        psp = mapper.toEntity(request);
        pspRepository.save(psp);

        LoggerConfig.LOGGER_PSP.info("Bank : " + psp.getBankName() + " created successfully!");

        return mapper.toResponse(psp);
    }

    @Transactional(readOnly = true)
    public PspResponse searchPspById(UUID pspId) {

        Psp psp = pspRepository.findById(pspId)
                .orElseThrow(() -> new ResourceNotFoundException("PSP not found with ID: " + pspId));

        LoggerConfig.LOGGER_PSP.info("Bank : " + psp.getBankName() + " returned successfully!");

        return mapper.toResponse(psp);
    }

    @Transactional(readOnly = true)
    public Psp searchPspEntity(UUID pspId) {
        return pspRepository.findById(pspId)
                .orElseThrow(() -> new ResourceNotFoundException("PSP not found with ID: " + pspId));
    }

}
