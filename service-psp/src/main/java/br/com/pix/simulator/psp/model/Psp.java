package br.com.pix.simulator.psp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "psps")
public class Psp {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pspId;

    @Column(nullable = false)
    private String bankName;

    @Column(nullable = false, unique = true, length = 3)
    private String bankCode;
}
