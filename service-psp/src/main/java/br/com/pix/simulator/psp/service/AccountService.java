package br.com.pix.simulator.psp.service;

import br.com.pix.simulator.psp.config.LoggerConfig;
import br.com.pix.simulator.psp.dto.account.AccountCreateRequest;
import br.com.pix.simulator.psp.dto.account.AccountResponse;
import br.com.pix.simulator.psp.dto.balance.BalanceResponse;
import br.com.pix.simulator.psp.dto.balance.DepositRequest;
import br.com.pix.simulator.psp.exception.ResourceNotFoundException;
import br.com.pix.simulator.psp.mapper.AccountMapper;
import br.com.pix.simulator.psp.model.Account;
import br.com.pix.simulator.psp.model.Psp;
import br.com.pix.simulator.psp.model.User;
import br.com.pix.simulator.psp.repository.AccountRepository;
import br.com.pix.simulator.psp.repository.PspRepository;
import br.com.pix.simulator.psp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class AccountService {

    private Account account;
    private final AccountMapper mapper;
    private final AccountRepository accountRepository;
    private final PspRepository pspRepository;
    private final UserRepository userRepository;

    public AccountService(AccountMapper mapper, AccountRepository accountRepository, PspRepository pspRepository, UserRepository userRepository) {
        this.mapper = mapper;
        this.accountRepository = accountRepository;
        this.pspRepository = pspRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public AccountResponse createAccount(AccountCreateRequest request) {

        Psp psp = pspRepository.findById(request.pspId())
                .orElseThrow(() -> new ResourceNotFoundException("PSP not found with ID: " + request.pspId()));

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + request.userId()));

        account = mapper.toEntity(request);
        accountRepository.save(account);

        LoggerConfig.LOGGER_ACCOUNT.info("Account : " + account.getAccountId() + " created successfully!");

        return mapper.toResponse(account);
    }

    @Transactional
    public BalanceResponse deposit(UUID accountId, DepositRequest request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountId));

        account.credit(request.value());
        accountRepository.save(account);

        LoggerConfig.LOGGER_ACCOUNT.info("Deposit : " + request.value() + " successfully completed!");

        return mapper.toBalanceResponse(account);
    }

    @Transactional(readOnly = true)
    public BalanceResponse checkBalance(UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountId));

        LoggerConfig.LOGGER_ACCOUNT.info("Balance : " + account.getBalance() + " successfully returned!");

        return mapper.toBalanceResponse(account);
    }

    //Methods Called by Events (RabbitMQ)

    @Transactional
    public void processDebit(UUID accountId, BigDecimal value) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Debit account not found: " + accountId));

        account.debit(value);

        LoggerConfig.LOGGER_ACCOUNT.info("Debit : " + value + " successfully completed!");

        accountRepository.save(account);
    }

    @Transactional
    public void processCredit(UUID accountId, BigDecimal value) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Credit account not found: " + accountId));

        account.credit(value);

        LoggerConfig.LOGGER_ACCOUNT.info("Credit : " + value + " successfully completed!");

        accountRepository.save(account);
    }

}
