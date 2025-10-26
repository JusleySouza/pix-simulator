package br.com.pix.simulator.psp.model;

import br.com.pix.simulator.psp.exception.InsufficientBalanceException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contas")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID accountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "psp_id", nullable = false)
    private Psp psp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 4)
    private String agency;

    @Column(nullable = false, length = 8)
    private String accountNumber;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Version
    private Long version;

}
