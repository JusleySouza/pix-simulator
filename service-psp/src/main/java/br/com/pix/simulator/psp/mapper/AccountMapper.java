package br.com.pix.simulator.psp.mapper;

import br.com.pix.simulator.psp.dto.account.AccountCreateRequest;
import br.com.pix.simulator.psp.dto.account.AccountResponse;
import br.com.pix.simulator.psp.dto.balance.BalanceResponse;
import br.com.pix.simulator.psp.model.Account;
import br.com.pix.simulator.psp.model.Psp;
import br.com.pix.simulator.psp.model.User;
import jakarta.persistence.EntityManager;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Generated
public class AccountMapper {

    @Autowired
    private EntityManager entityManager;

    public Account toEntity(AccountCreateRequest request) {

        if (request == null) {
            return null;
        }

        Account account = new Account();
        account.setPsp(entityManager.getReference(Psp.class, request.pspId()));
        account.setUser(entityManager.getReference(User.class, request.userId()));
        account.setAgency(request.agency());
        account.setAccountNumber(request.accountNumber());
        account.setBalance(request.initialBalance());
        return account;
    }


    public AccountResponse toResponse(Account account) {

        if (account == null) {
            return null;
        }
        return new AccountResponse(
                account.getAccountId(),
                account.getPsp() != null ? account.getPsp().getBankName() : null,
                account.getUser() != null ? account.getUser().getName() : null,
                account.getAgency(),
                account.getAccountNumber(),
                account.getBalance()
        );
    }

    public BalanceResponse toBalanceResponse(Account account) {

        if (account == null) {
            return null;
        }
        return new BalanceResponse(
                account.getAccountId(),
                account.getBalance()
        );
    }

}
