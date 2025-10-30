package br.com.pix.simulator.psp.mapper;

import br.com.pix.simulator.psp.dto.psps.PspCreateRequest;
import br.com.pix.simulator.psp.dto.psps.PspResponse;
import br.com.pix.simulator.psp.model.Psp;
import lombok.Generated;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Generated
public class PspMapper {

    public Psp toEntity(PspCreateRequest request) {

        if (request == null) {
            return null;
        }

        Psp psp = new Psp();
       // psp.setPspId(UUID.randomUUID());
        psp.setBankName(request.bankName());
        psp.setBankCode(request.bankCode());
        return psp;
    }


    public PspResponse toResponse(Psp psp) {

        if (psp == null) {
            return null;
        }
        return new PspResponse(
                psp.getPspId(),
                psp.getBankName(),
                psp.getBankCode()
        );
    }

}
