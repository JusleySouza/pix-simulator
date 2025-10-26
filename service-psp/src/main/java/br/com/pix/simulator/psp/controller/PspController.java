package br.com.pix.simulator.psp.controller;

import br.com.pix.simulator.psp.dto.psps.PspCreateRequest;
import br.com.pix.simulator.psp.dto.psps.PspResponse;
import br.com.pix.simulator.psp.service.PspService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/psps")
public class PspController {

    private final PspService pspService;

    public PspController(PspService pspService) {
        this.pspService = pspService;
    }

    @PostMapping
    public ResponseEntity<PspResponse> createPsp(@Valid @RequestBody PspCreateRequest request) {
        return new ResponseEntity<>(pspService.createPsp(request), HttpStatus.CREATED);
    }

    @GetMapping("/{pspId}")
    public ResponseEntity<PspResponse> searchPsp(@PathVariable UUID pspId) {
        return ResponseEntity.ok(pspService.searchPspById(pspId));
    }

}
