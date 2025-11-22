package br.com.pix.simulator.dict.controller;

import br.com.pix.simulator.dict.dto.KeyCreateRequest;
import br.com.pix.simulator.dict.dto.KeyResponse;
import br.com.pix.simulator.dict.service.PixKeyService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/keys")
public class PixKeyController {

    private final PixKeyService pixKeyService;

    public PixKeyController(PixKeyService pixKeyService) {
        this.pixKeyService = pixKeyService;
    }

    @PostMapping
    @Operation(summary = "Register a new Pix Key")
    public ResponseEntity<KeyResponse> createKey(@Valid @RequestBody KeyCreateRequest request) {
        KeyResponse response = pixKeyService.createKey(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{keyValue}")
    @Operation(summary = "Search for a Pix Key")
    public ResponseEntity<KeyResponse> findKey(@PathVariable String keyValue) {
        KeyResponse response = pixKeyService.findKey(keyValue);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{keyValue}")
    @Operation(summary = "Delete a Pix Key")
    public ResponseEntity<Void> deleteKey(@PathVariable String keyValue) {
        pixKeyService.deleteKey(keyValue);
        return ResponseEntity.noContent().build();
    }

}
