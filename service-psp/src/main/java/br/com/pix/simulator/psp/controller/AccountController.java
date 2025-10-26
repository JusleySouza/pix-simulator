package br.com.pix.simulator.psp.controller;

import br.com.pix.simulator.psp.dto.account.AccountCreateRequest;
import br.com.pix.simulator.psp.dto.account.AccountResponse;
import br.com.pix.simulator.psp.dto.balance.BalanceResponse;
import br.com.pix.simulator.psp.dto.balance.DepositRequest;
import br.com.pix.simulator.psp.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountCreateRequest request ) {
        return new ResponseEntity<>(accountService.createAccount(request), HttpStatus.CREATED);
    }

    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<BalanceResponse> deposit(@PathVariable UUID accountId, @Valid @RequestBody DepositRequest request) {
        return ResponseEntity.ok(accountService.deposit(accountId, request));
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<BalanceResponse> checkBalance(@PathVariable UUID accountId) {
        return ResponseEntity.ok(accountService.checkBalance(accountId));
    }

}
