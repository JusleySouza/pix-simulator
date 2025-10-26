package br.com.pix.simulator.psp.repository;

import br.com.pix.simulator.psp.model.Psp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PspRepository extends JpaRepository<Psp, UUID> { }
