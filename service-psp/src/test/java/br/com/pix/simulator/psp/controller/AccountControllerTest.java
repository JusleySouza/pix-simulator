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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
                accountId, "Bank Test", "Client Test", "0001", "01234567", BigDecimal.TEN
        );

        when(service.createAccount(any(AccountCreateRequest.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());

        verify(service, times(1)).createAccount(any(AccountCreateRequest.class));
    }

    @Test
    @DisplayName("Creating a Account with an empty agency should fail and return a 400 Bad Request status.")
    void createAccount_WhenAgencyIsEmpty_ShouldReturnBadRequest() throws Exception {

        AccountCreateRequest invalidRequestDto = new AccountCreateRequest(
                UUID.randomUUID(), UUID.randomUUID(), null, "01234567", BigDecimal.TEN
        );

        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("agency"))
                .andExpect(jsonPath("$.errors[0].message").value( "Agency is required."));

        verify(service, never()).createAccount(any());
    }

    @Test
    @DisplayName("Creating an Account with a null pspId should fail and return a 400 Bad Request status.")
    void createAccount_WhenPspIdIsNull_ShouldReturnBadRequest() throws Exception {

        AccountCreateRequest invalidRequestDto = new AccountCreateRequest(
                null, UUID.randomUUID(), "0001", "01234567", BigDecimal.TEN
        );

        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("pspId"))
                .andExpect(jsonPath("$.errors[0].message").value( "Psp id is required."));

        verify(service, never()).createAccount(any());
    }

    @Test
    @DisplayName("Creating an Account with a null userId should fail and return a 400 Bad Request status.")
    void createAccount_WhenUserIdIsNull_ShouldReturnBadRequest() throws Exception {

        AccountCreateRequest invalidRequestDto = new AccountCreateRequest(
                UUID.randomUUID(), null, "0001", "01234567", BigDecimal.TEN
        );

        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("userId"))
                .andExpect(jsonPath("$.errors[0].message").value( "User id is required."));

        verify(service, never()).createAccount(any());
    }

    @Test
    @DisplayName("Creating an Account with a shorter agency should fail and return a 400 Bad Request status.")
    void createAccount_WhenAgencyIsShorter_ShouldReturnBadRequest() throws Exception {

        AccountCreateRequest invalidRequestDto = new AccountCreateRequest(
                UUID.randomUUID(), UUID.randomUUID(), "001", "01234567", BigDecimal.TEN
        );

        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("agency"))
                .andExpect(jsonPath("$.errors[0].message").value( "The agency must contain 4 digits."));

        verify(service, never()).createAccount(any());
    }

    @Test
    @DisplayName("Creating an Account with a larger agency should fail and return a 400 Bad Request status.")
    void createAccount_WhenAgencyIsLarger_ShouldReturnBadRequest() throws Exception {

        AccountCreateRequest invalidRequestDto = new AccountCreateRequest(
                UUID.randomUUID(), UUID.randomUUID(), "00012", "01234567", BigDecimal.TEN
        );

        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("agency"))
                .andExpect(jsonPath("$.errors[0].message").value( "The agency must contain 4 digits."));

        verify(service, never()).createAccount(any());
    }

    @Test
    @DisplayName("Creating a Account with an empty accountNumber should fail and return a 400 Bad Request status.")
    void createAccount_WhenAccountNumberIsEmpty_ShouldReturnBadRequest() throws Exception {

        AccountCreateRequest invalidRequestDto = new AccountCreateRequest(
                UUID.randomUUID(), UUID.randomUUID(), "0001", null, BigDecimal.TEN
        );

        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("accountNumber"))
                .andExpect(jsonPath("$.errors[0].message").value("Account number is required."));

        verify(service, never()).createAccount(any());
    }

    @Test
    @DisplayName("Creating an Account with a shorter accountNumber should fail and return a 400 Bad Request status.")
    void createAccount_WhenAccountNumberIsShorter_ShouldReturnBadRequest() throws Exception {

        AccountCreateRequest invalidRequestDto = new AccountCreateRequest(
                UUID.randomUUID(), UUID.randomUUID(), "0001", "0123456", BigDecimal.TEN
        );

        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("accountNumber"))
                .andExpect(jsonPath("$.errors[0].message").value("The account number must contain 8 digits."));

        verify(service, never()).createAccount(any());
    }

    @Test
    @DisplayName("Creating an Account with a larger accountNumber should fail and return a 400 Bad Request status.")
    void createAccount_WhenAccountNumberIsLarger_ShouldReturnBadRequest() throws Exception {

        AccountCreateRequest invalidRequestDto = new AccountCreateRequest(
                UUID.randomUUID(), UUID.randomUUID(), "0001", "012345678", BigDecimal.TEN
        );

        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("accountNumber"))
                .andExpect(jsonPath("$.errors[0].message").value("The account number must contain 8 digits."));

        verify(service, never()).createAccount(any());
    }

    @Test
    @DisplayName("Creating an Account with a null balance initial should fail and return a 400 Bad Request status.")
    void createAccount_WhenInitialBalanceIsNull_ShouldReturnBadRequest() throws Exception {

        AccountCreateRequest invalidRequestDto = new AccountCreateRequest(
                UUID.randomUUID(), UUID.randomUUID(), "0001", "01234567", null
        );

        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("initialBalance"))
                .andExpect(jsonPath("$.errors[0].message").value("Initial balance is required."));

        verify(service, never()).createAccount(any());
    }

    @Test
    @DisplayName("Creating an Account with a negative balance initial should fail and return a 400 Bad Request status.")
    void createAccount_WhenInitialBalanceIsNegative_ShouldReturnBadRequest() throws Exception {

        AccountCreateRequest invalidRequestDto = new AccountCreateRequest(
                UUID.randomUUID(), UUID.randomUUID(), "0001", "01234567", new BigDecimal(-150)
        );

        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("initialBalance"))
                .andExpect(jsonPath("$.errors[0].message").value("The initial balance must be greater than zero."));

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
    @DisplayName("It may fail to deposit is null and return a 400 Bad Request status.")
    void deposit_WhenDepositIsNull_ShouldReturnBadRequest() throws Exception {
        UUID accountId = UUID.randomUUID();
        DepositRequest requestDto = new DepositRequest(null);

        mockMvc.perform(post("/api/v1/accounts/{accountId}/deposit", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("value"))
                .andExpect(jsonPath("$.errors[0].message").value("Deposit value is required."));

        verify(service, never()).deposit(any(UUID.class), any(DepositRequest.class));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when deposit value is negative")
    void deposit_WhenValueIsNegative_ShouldReturnBadRequest() throws Exception {
        UUID accountId = UUID.randomUUID();
        DepositRequest requestDto = new DepositRequest(new BigDecimal("-100.00"));

        mockMvc.perform(post("/api/v1/accounts/{accountId}/deposit", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("value"))
                .andExpect(jsonPath("$.errors[0].message").value("The deposit value must be greater than zero."));

        verify(service, never()).deposit(any(UUID.class), any(DepositRequest.class));
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
