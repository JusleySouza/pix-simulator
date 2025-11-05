package br.com.pix.simulator.psp.service;

import br.com.pix.simulator.psp.dto.account.AccountCreateRequest;
import br.com.pix.simulator.psp.dto.account.AccountResponse;
import br.com.pix.simulator.psp.dto.balance.BalanceResponse;
import br.com.pix.simulator.psp.dto.balance.DepositRequest;
import br.com.pix.simulator.psp.exception.InsufficientBalanceException;
import br.com.pix.simulator.psp.exception.ResourceNotFoundException;
import br.com.pix.simulator.psp.exception.ValidationException;
import br.com.pix.simulator.psp.mapper.AccountMapper;
import br.com.pix.simulator.psp.model.Account;
import br.com.pix.simulator.psp.model.Psp;
import br.com.pix.simulator.psp.model.User;
import br.com.pix.simulator.psp.repository.AccountRepository;
import br.com.pix.simulator.psp.repository.PspRepository;
import br.com.pix.simulator.psp.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountMapper mapper;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private PspRepository pspRepository;
    @Mock
    private UserRepository userRepository;

    @Mock
    private Account account;
    @Mock
    private Psp psp;
    @Mock
    private User user;

    @InjectMocks
    private AccountService accountService;

    @Test
    @DisplayName("You should be able to successfully create an account when PSP and User exist.")
    void createAccount_shouldSucceed_whenDataIsValid() {
        UUID pspId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        AccountCreateRequest request = new AccountCreateRequest(pspId, userId, "0001", "01234567", BigDecimal.TEN);

        when(mapper.toEntity(request)).thenReturn(account);

        when(pspRepository.findById(pspId)).thenReturn(Optional.of(psp));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        AccountResponse expectedResponse = new AccountResponse(UUID.randomUUID(), "PSP Test", "User Test", "0001", "01234567", BigDecimal.TEN);
        when(mapper.toResponse(account)).thenReturn(expectedResponse);

        when(accountRepository.save(account)).thenReturn(account);

        AccountResponse result = accountService.createAccount(request);

        assertNotNull(result);
        assertEquals(expectedResponse.accountId(), result.accountId());

        verify(pspRepository, times(1)).findById(pspId);
        verify(userRepository, times(1)).findById(userId);
        verify(accountRepository, times(1)).save(account);
        verify(mapper, times(1)).toEntity(request);
        verify(mapper, times(1)).toResponse(account);
    }

    @Test
    @DisplayName("createAccount should throw ResourceNotFoundException if the PSP is not found.")
    void createAccount_shouldThrowResourceNotFound_whenPspNotFound() {
        UUID pspId = UUID.randomUUID();
        AccountCreateRequest request = new AccountCreateRequest(pspId, UUID.randomUUID(), "0001", "01234567", BigDecimal.TEN);

        when(pspRepository.findById(pspId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> accountService.createAccount(request)
        );

        assertTrue(exception.getMessage().contains("PSP not found with ID: " + request.pspId()));

        verify(accountRepository, never()).save(any());
        verify(userRepository, never()).findById(any());
    }

    @Test
    @DisplayName("createAccount should throw ResourceNotFoundException if the User is not found.")
    void createAccount_shouldThrowResourceNotFound_whenUserNotFound() {
        UUID pspId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        AccountCreateRequest request = new AccountCreateRequest(pspId, userId, "0001", "01234567", BigDecimal.TEN);

        when(pspRepository.findById(pspId)).thenReturn(Optional.of(psp));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> accountService.createAccount(request)
        );

        assertTrue(exception.getMessage().contains("User not found with ID: " + request.userId()));
        verify(accountRepository, never()).save(any());
    }

    @Test
    @DisplayName("You should be able to deposit successfully if the account exists.")
    void deposit_shouldSucceed_whenAccountExists() {
        UUID accountId = UUID.randomUUID();
        DepositRequest request = new DepositRequest(BigDecimal.TEN);
        BalanceResponse expectedResponse = new BalanceResponse(accountId, BigDecimal.valueOf(110));

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(mapper.toBalanceResponse(account)).thenReturn(expectedResponse);

        BalanceResponse result = accountService.deposit(accountId, request);

        assertNotNull(result);
        assertEquals(expectedResponse.balance(), result.balance());

        verify(account, times(1)).credit(BigDecimal.TEN);
        verify(accountRepository, times(1)).save(account);
        verify(mapper, times(1)).toBalanceResponse(account);
    }

    @Test
    @DisplayName("The deposit should throw a ResourceNotFoundException if the account is not found.")
    void deposit_shouldThrowResourceNotFound_whenAccountNotFound() {
        UUID accountId = UUID.randomUUID();
        DepositRequest request = new DepositRequest(BigDecimal.TEN);
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            accountService.deposit(accountId, request);
        });

        verify(accountRepository, never()).save(any());
        verify(mapper, never()).toBalanceResponse(any());
    }

    @Test
    @DisplayName("It should successfully return the balance when the account exists.")
    void checkBalance_shouldSucceed_whenAccountExists() {
        UUID accountId = UUID.randomUUID();
        BalanceResponse expectedResponse = new BalanceResponse(accountId, BigDecimal.valueOf(100));

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(mapper.toBalanceResponse(account)).thenReturn(expectedResponse);

        BalanceResponse result = accountService.checkBalance(accountId);

        assertNotNull(result);
        assertEquals(expectedResponse.balance(), result.balance());
        verify(accountRepository, times(1)).findById(accountId);
        verify(mapper, times(1)).toBalanceResponse(account);
        verify(account, never()).credit(any());
        verify(account, never()).debit(any());
    }

    @Test
    @DisplayName("The balance should throw a ResourceNotFoundException if the account is not found.")
    void balance_shouldThrowResourceNotFound_whenAccountNotFound() {
        UUID accountId = UUID.randomUUID();
        BalanceResponse expectedResponse = new BalanceResponse(accountId, BigDecimal.valueOf(100));
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            accountService.checkBalance(accountId);
        });

        verify(accountRepository, never()).save(any());
        verify(mapper, never()).toBalanceResponse(any());
    }

    @Test
    @DisplayName("processDebit should debit successfully when the account exists and has a balance.")
    void processDebit_shouldSucceed_whenAccountExistsAndHasBalance() {
        UUID accountId = UUID.randomUUID();
        BigDecimal debitValue = BigDecimal.valueOf(50);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        doNothing().when(account).debit(debitValue);

        accountService.processDebit(accountId, debitValue);

        verify(accountRepository, times(1)).findById(accountId);
        verify(account, times(1)).debit(debitValue);
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    @DisplayName("processDebit should throw BalanceInsufficientException if the debit fails.")
    void processDebit_shouldThrowException_whenAccountHasNoBalance() {
        UUID accountId = UUID.randomUUID();
        BigDecimal debitValue = BigDecimal.valueOf(1000);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        doThrow(new InsufficientBalanceException("Insufficient account balance " + accountId))
                .when(account).debit(debitValue);

        assertThrows(InsufficientBalanceException.class, () -> {
            accountService.processDebit(accountId, debitValue);
        });

        verify(accountRepository, never()).save(account);
    }

    @Test
    @DisplayName("The processDebit should throw a ResourceNotFoundException if the account is not found.")
    void processDebit_shouldThrowResourceNotFound_whenAccountNotFound() {
        UUID accountId = UUID.randomUUID();
        BigDecimal debitValue = BigDecimal.valueOf(50);

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            accountService.processDebit(accountId, debitValue);
        });

        verify(accountRepository, never()).save(any());
        verify(mapper, never()).toBalanceResponse(any());
    }

    @Test
    @DisplayName("The processCredit function should successfully credit the account when it exists.")
    void processCredit_shouldSucceed_whenAccountExists() {
        UUID accountId = UUID.randomUUID();
        BigDecimal creditValue = BigDecimal.valueOf(50);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        doNothing().when(account).credit(creditValue);

        accountService.processCredit(accountId, creditValue);

        verify(accountRepository, times(1)).findById(accountId);
        verify(account, times(1)).credit(creditValue);
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    @DisplayName("The processCredit should throw a ResourceNotFoundException if the account is not found.")
    void processCredit_shouldThrowResourceNotFound_whenAccountNotFound() {
        UUID accountId = UUID.randomUUID();
        BigDecimal creditValue = BigDecimal.valueOf(50);

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            accountService.processCredit(accountId, creditValue);
        });

        verify(accountRepository, never()).save(any());
        verify(mapper, never()).toBalanceResponse(any());
    }

}
