package br.com.pix.simulator.psp.repository;

import br.com.pix.simulator.psp.exception.InsufficientBalanceException;
import br.com.pix.simulator.psp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> { }