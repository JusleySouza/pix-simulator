package br.com.pix.simulator.psp.repository;

import br.com.pix.simulator.psp.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> { }
