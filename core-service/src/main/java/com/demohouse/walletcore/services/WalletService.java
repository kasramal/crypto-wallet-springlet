package com.demohouse.walletcore.services;

import com.demohouse.walletcore.core.hdwallets.HDWallet;
import com.demohouse.walletcore.core.providers.CryptoCurrencyApiProvider;
import com.demohouse.walletcore.core.transactions.services.AccountStatus;
import com.demohouse.walletcore.entities.Coin;
import com.demohouse.walletcore.services.dto.WalletBalanceDTO;

import java.util.Map;

public interface WalletService {

    String generateWallet(Coin coin);

    String generateMnemonic();

    WalletBalanceDTO checkBalance(Coin coin, String address);

    AccountStatus checkFinalBalance(Coin coin, String address);

    Map<CryptoCurrencyApiProvider, AccountStatus> checkFinalBalanceMap(Coin coin, String address);

    HDWallet generateHDWallet(Coin coin, String password);

    HDWallet retrieveHDWallet(Coin coin, String mnemonic, String password);
}
