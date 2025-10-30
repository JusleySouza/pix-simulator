package br.com.pix.simulator.psp.controller;

import br.com.pix.simulator.psp.dto.psps.PspCreateRequest;
import br.com.pix.simulator.psp.dto.psps.PspResponse;
import br.com.pix.simulator.psp.service.PspService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/psps")
@Tag(name = "Bank", description = "Information related to the bank")
public class PspController {

    private final PspService pspService;

    public PspController(PspService pspService) {
        this.pspService = pspService;
    }

    @PostMapping
    @Operation(summary = "Create a bank", description = "Create a new bank with the provided details.")
    public ResponseEntity<PspResponse> createPsp(@Valid @RequestBody PspCreateRequest request) {
        return new ResponseEntity<>(pspService.createPsp(request), HttpStatus.CREATED);
    }

    @GetMapping("/{pspId}")
    @Operation(summary = "Search the bank by ID.", description = "Returns the bank data")
    public ResponseEntity<PspResponse> searchPsp(@PathVariable UUID pspId) {
        return ResponseEntity.ok(pspService.searchPspById(pspId));
    }

}
