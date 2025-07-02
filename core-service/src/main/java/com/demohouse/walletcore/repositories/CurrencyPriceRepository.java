package com.demohouse.walletcore.repositories;

import com.demohouse.walletcore.entities.CurrencyPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyPriceRepository extends JpaRepository<CurrencyPrice, Long> {
    CurrencyPrice findByIso(String iso);
}
