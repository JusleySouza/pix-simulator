package br.com.pix.simulator.psp.service;

import br.com.pix.simulator.psp.dto.account.AccountCreateRequest;
import br.com.pix.simulator.psp.dto.account.AccountResponse;
import br.com.pix.simulator.psp.model.Account;
import br.com.pix.simulator.psp.model.Psp;
import br.com.pix.simulator.psp.model.User;
import br.com.pix.simulator.psp.repository.AccountRepository;
import br.com.pix.simulator.psp.repository.PspRepository;
import br.com.pix.simulator.psp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final PspRepository pspRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, PspRepository pspRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.pspRepository = pspRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public AccountResponse createAccount(AccountCreateRequest request) {

        Psp psp = pspRepository.findById(request.pspId())
                .orElseThrow(() -> new EntityNotFoundException("PSP not found with ID: " + request.pspId()));

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + request.userId()));

        Account newAccount = new Account();
        newAccount.setPsp(psp);
        newAccount.setUser(user);
        newAccount.setAgency(request.agency());
        newAccount.setAccountNumber(request.accountNumber());
        newAccount.setBalance(request.initialBalance());

        Account savedAccount = accountRepository.save(newAccount);

        return new AccountResponse(
                savedAccount.getAccountId(),
                psp.getBankName(),
                user.getName(),
                savedAccount.getAgency(),
                savedAccount.getAccountNumber(),
                savedAccount.getBalance()
        );
    }

}
