package br.com.pix.simulator.psp.controller;

import br.com.pix.simulator.psp.dto.account.AccountCreateRequest;
import br.com.pix.simulator.psp.dto.account.AccountResponse;
import br.com.pix.simulator.psp.mapper.AccountMapper;
import br.com.pix.simulator.psp.model.Account;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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


}
