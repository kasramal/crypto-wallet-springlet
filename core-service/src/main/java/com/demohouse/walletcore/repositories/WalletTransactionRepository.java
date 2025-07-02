package com.demohouse.walletcore.repositories;

import com.demohouse.walletcore.entities.Coin;
import com.demohouse.walletcore.entities.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {

    Optional<WalletTransaction> findByCoinAndTransactionId(Coin coin,String transactionId);
}
