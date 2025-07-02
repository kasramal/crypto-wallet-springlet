package com.demohouse.walletcore.entities;

import com.demohouse.walletcore.core.providers.CryptoCurrencyApiProvider;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletTransaction implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Wallet from;
    @Column(name = "to_address", nullable = false)
    private String to;
    @Column(nullable = false, precision = 16, scale = 9)
    private BigDecimal value;
    @Column(nullable = false, precision = 16, scale = 9)
    private BigDecimal fee;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CryptoCurrencyApiProvider provider;
    @Column(nullable = false)
    private String transactionId;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Coin coin;
    private boolean confirmed;
    @Column(nullable = false)
    private ZonedDateTime actionDate;
}
