package br.com.pix.simulator.psp.service;

import br.com.pix.simulator.psp.dto.account.AccountCreateRequest;
import br.com.pix.simulator.psp.dto.account.AccountResponse;
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

}
