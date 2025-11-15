package br.com.pix.simulator.dict.repository;

import br.com.pix.simulator.dict.model.PixKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PixKeyRepository extends JpaRepository<PixKey, String> {

    Optional<PixKey> findByAccountId(UUID accountId);

}
