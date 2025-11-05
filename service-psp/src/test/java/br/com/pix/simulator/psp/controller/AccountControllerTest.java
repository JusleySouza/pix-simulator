package br.com.pix.simulator.psp.controller;

import br.com.pix.simulator.psp.dto.account.AccountCreateRequest;
import br.com.pix.simulator.psp.dto.account.AccountResponse;
import br.com.pix.simulator.psp.dto.balance.BalanceResponse;
import br.com.pix.simulator.psp.dto.balance.DepositRequest;
import br.com.pix.simulator.psp.exception.ResourceNotFoundException;
import br.com.pix.simulator.psp.mapper.AccountMapper;
import br.com.pix.simulator.psp.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AccountService service;

    @MockitoBean
    private AccountMapper mapper;


    @Test
    @DisplayName("You should successfully create an account and receive a status of 201 Created.")
    void createAccount_WhenValidRequest_ShouldReturnCreated() throws Exception {

        UUID accountId = UUID.randomUUID();
        AccountCreateRequest requestDto = new AccountCreateRequest(
                UUID.randomUUID(), UUID.randomUUID(), "0001", "01234567", BigDecimal.TEN
        );

        AccountResponse responseDto = new AccountResponse(
                accountId, "Banco Teste", "Cliente Teste", "0001", "01234567", BigDecimal.TEN
        );

        when(service.createAccount(any(AccountCreateRequest.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());

        verify(service, times(1)).createAccount(any(AccountCreateRequest.class));
    }

    @Test
    @DisplayName("It should fail to create an account with invalid data and return a 400 Bad Request status.")
    void createAccount_WhenInvalidRequest_ShouldReturnBadRequest() throws Exception {

        AccountCreateRequest invalidRequestDto = new AccountCreateRequest(
                null, null, null, "12345", BigDecimal.TEN
        );

        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest());

        verify(service, never()).createAccount(any());
    }

    @Test
    @DisplayName("You should successfully complete the deposit and receive a status of 200 OK.")
    void deposit_WhenValidRequest_ShouldReturnOk() throws Exception {
        UUID accountId = UUID.randomUUID();
        DepositRequest requestDto = new DepositRequest(BigDecimal.valueOf(100));
        BalanceResponse responseDto = new BalanceResponse(accountId, BigDecimal.valueOf(110));

        when(service.deposit(eq(accountId), any(DepositRequest.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/accounts/{accountId}/deposit", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        verify(service, times(1)).deposit(eq(accountId), any(DepositRequest.class));
    }

    @Test
    @DisplayName("It may fail to deposit into a non-existent account and return a 404 Not Found error.")
    void deposit_WhenAccountNotFound_ShouldReturnNotFound() throws Exception {
        UUID accountId = UUID.randomUUID();
        DepositRequest requestDto = new DepositRequest(BigDecimal.valueOf(100));

        when(service.deposit(eq(accountId), any(DepositRequest.class)))
                .thenThrow(new ResourceNotFoundException("Account not found: " + accountId));

        mockMvc.perform(post("/api/v1/accounts/{accountId}/deposit", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound());

        verify(service, times(1)).deposit(eq(accountId), any(DepositRequest.class));
    }

    @Test
    @DisplayName("You should be able to check your balance successfully and receive a status of 200 OK.")
    void checkBalance_WhenAccountExists_ShouldReturnOk() throws Exception {
        UUID accountId = UUID.randomUUID();
        BalanceResponse responseDto = new BalanceResponse(accountId, BigDecimal.valueOf(500.75));

        when(service.checkBalance(accountId)).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/accounts/{accountId}/balance", accountId))
                .andExpect(status().isOk());

        verify(service, times(1)).checkBalance(accountId);
    }

    @Test
    @DisplayName("It may fail to check for a non-existent account balance and return a 404 Not Found error.")
    void checkBalance_WhenAccountNotFound_ShouldReturnNotFound() throws Exception {
        UUID accountId = UUID.randomUUID();

        when(service.checkBalance(accountId))
                .thenThrow(new ResourceNotFoundException("Account not found: " + accountId));

        mockMvc.perform(get("/api/v1/accounts/{accountId}/balance", accountId))
                .andExpect(status().isNotFound());

        verify(service, times(1)).checkBalance(accountId);
    }

}
