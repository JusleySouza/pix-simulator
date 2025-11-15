package br.com.pix.simulator.dict.model;


import br.com.pix.simulator.dict.model.enums.AccountType;
import br.com.pix.simulator.dict.model.enums.KeyStatus;
import br.com.pix.simulator.dict.model.enums.OwnerType;
import br.com.pix.simulator.dict.model.enums.PixKeyType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Generated
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pix_keys")
public class PixKey {

    @Id
    @Column(name = "key_value", nullable = false, unique = true, length = 100)
    private String keyValue;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "key_type", nullable = false, length = 10)
    private PixKeyType keyType;

    @NotNull
    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @NotNull
    @Column(name = "psp_id", nullable = false)
    private UUID pspId;

    @Enumerated(EnumType.STRING)
    @Column(name = "key_status", nullable = false, length = 10)
    private KeyStatus keyStatus = KeyStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false, length = 10)
    private AccountType accountType = AccountType.CHECKING;

    @Enumerated(EnumType.STRING)
    @Column(name = "owner_type", nullable = false, length = 10)
    private OwnerType ownerType = OwnerType.INDIVIDUAL;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
