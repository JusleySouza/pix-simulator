package br.com.pix.simulator.psp.service;

import br.com.pix.simulator.psp.dto.psps.PspCreateRequest;
import br.com.pix.simulator.psp.dto.psps.PspResponse;
import br.com.pix.simulator.psp.exception.ResourceNotFoundException;
import br.com.pix.simulator.psp.model.Psp;
import br.com.pix.simulator.psp.repository.PspRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PspService {

    private final PspRepository pspRepository;

    public PspService(PspRepository pspRepository) {
        this.pspRepository = pspRepository;
    }

    @Transactional
    public PspResponse createPsp(PspCreateRequest request) {
        if(pspRepository.findByBankCode(request.bankCode()).isPresent()){
            throw new IllegalArgumentException("There is already a PSP with the bank code: " + request.bankCode());
        }

        Psp psp = new Psp(
                null,
                request.bankName(),
                request.bankCode()
        );

        Psp savedPsp = pspRepository.save(psp);

        return new PspResponse(
                savedPsp.getPspId(),
                savedPsp.getBankName(),
                savedPsp.getBankCode()
        );
    }

    @Transactional(readOnly = true)
    public PspResponse searchPspById(UUID pspId) {

        Psp psp = pspRepository.findById(pspId)
                .orElseThrow(() -> new ResourceNotFoundException("PSP not found with ID: " + pspId));

        return new PspResponse(psp.getPspId(), psp.getBankName(), psp.getBankCode());
    }

}
