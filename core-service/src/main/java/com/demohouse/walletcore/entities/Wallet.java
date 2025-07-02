package com.demohouse.walletcore.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;


@Getter
@Setter
@Entity
@Table
public class Wallet {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Coin coin;

    @Column(nullable = false)
    private String privateKey;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private ZonedDateTime createdDate;
    private String mnemonic;
}
