package com.demohouse.walletcore.repositories;

import com.demohouse.walletcore.entities.Coin;
import com.demohouse.walletcore.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByCoinAndAddress(Coin coin, String address);
}
