package br.com.pix.simulator.psp.repository;

import br.com.pix.simulator.psp.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    @Query("SELECT a FROM Account a JOIN FETCH a.user u JOIN FETCH a.psp p WHERE a.accountId = :accountId AND u.userId = :userId AND p.pspId = :pspId")
    Optional<Account> findByValidationKeys(
            @Param("accountId") UUID accountId,
            @Param("userId") UUID userId,
            @Param("pspId") UUID pspId // Tipo ajustado para UUID
    );

}
