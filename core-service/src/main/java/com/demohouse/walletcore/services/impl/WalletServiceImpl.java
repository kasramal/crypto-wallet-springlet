package com.demohouse.walletcore.services.impl;

import com.demohouse.walletcore.core.addresses.AddressGenerator;
import com.demohouse.walletcore.core.addresses.AddressGeneratorFactory;
import com.demohouse.walletcore.core.addresses.GeneratedAddress;
import com.demohouse.walletcore.core.hdwallets.HDWallet;
import com.demohouse.walletcore.core.hdwallets.HDWalletFactory;
import com.demohouse.walletcore.core.hdwallets.HDWalletGenerator;
import com.demohouse.walletcore.core.hdwallets.MnemonicHelper;
import com.demohouse.walletcore.core.manager.CoinManager;
import com.demohouse.walletcore.core.manager.CoinManagerFactory;
import com.demohouse.walletcore.core.providers.CryptoCurrencyApiProvider;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyError;
import com.demohouse.walletcore.core.transactions.services.AccountStatus;
import com.demohouse.walletcore.entities.Coin;
import com.demohouse.walletcore.entities.Wallet;
import com.demohouse.walletcore.repositories.WalletRepository;
import com.demohouse.walletcore.services.WalletService;
import com.demohouse.walletcore.services.dto.WalletBalanceDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.Map;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final CoinManagerFactory coinManagerFactory;

    public WalletServiceImpl(WalletRepository walletRepository,
                             CoinManagerFactory coinManagerFactory) {
        this.walletRepository = walletRepository;
        this.coinManagerFactory = coinManagerFactory;
    }

    @Override
    public String generateWallet(Coin coin) {
        AddressGenerator addressGenerator = AddressGeneratorFactory.getAddressGenerator(coin);
        try {
            GeneratedAddress generatedAddress = addressGenerator.generate();
            Wallet wallet = new Wallet();
            wallet.setCoin(coin);
            wallet.setAddress(generatedAddress.getAddress());
            wallet.setPrivateKey(generatedAddress.getPrivateKey());
            wallet.setCreatedDate(ZonedDateTime.now());
            this.walletRepository.save(wallet);
            return generatedAddress.getAddress();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            throw new CryptoCurrencyApiException(CryptoCurrencyError.ADDRESS_GENERATE_ERROR);
        }
    }

    @Override
    public HDWallet generateHDWallet(Coin coin, String password) {
        HDWalletGenerator hdWalletGenerator = HDWalletFactory.getHDWalletGenerator(coin);
        HDWallet generatedHdWallet = hdWalletGenerator.generate(coin, password);
        persistHDWallet(coin, generatedHdWallet);
        return generatedHdWallet;
    }

    @Override
    public HDWallet retrieveHDWallet(Coin coin, String mnemonic, String password) {
        HDWalletGenerator hdWalletGenerator = HDWalletFactory.getHDWalletGenerator(coin);
        HDWallet generatedHdWallet = hdWalletGenerator.retrieve(coin, mnemonic, password);
        persistHDWallet(coin, generatedHdWallet);
        return generatedHdWallet;
    }

    public void persistHDWallet(Coin coin, HDWallet generatedHdWallet) {
        Wallet wallet = new Wallet();
        if (walletRepository.findByCoinAndAddress(coin, generatedHdWallet.getHdWalletAddress().getAddress()).isPresent())
            return;
        wallet.setCoin(coin);
        wallet.setAddress(generatedHdWallet.getHdWalletAddress().getAddress());
        wallet.setPrivateKey(generatedHdWallet.getHdWalletAddress().getPrivateKey());
        wallet.setMnemonic(generatedHdWallet.getCredentials().getMnemonic());
        wallet.setCreatedDate(ZonedDateTime.now());
        this.walletRepository.save(wallet);
    }

    @Override
    public String generateMnemonic() {
        return MnemonicHelper.generateMnemonic();
    }

    @Override
    public WalletBalanceDTO checkBalance(Coin coin, String address) {
        CoinManager manager = coinManagerFactory.getManager(coin);
        BigDecimal macroValue = manager.getAddressBalance(address);
        BigDecimal microValue = coin.convertMacroToMicroValue(macroValue);
        WalletBalanceDTO walletBalanceDTO = new WalletBalanceDTO();
        walletBalanceDTO.setMacro(macroValue);
        walletBalanceDTO.setMicro(microValue);
        return walletBalanceDTO;
    }

    @Override
    public AccountStatus checkFinalBalance(Coin coin, String address) {
        CoinManager manager = coinManagerFactory.getManager(coin);
        return manager.getTransactionStatus(address);
    }

    @Override
    public Map<CryptoCurrencyApiProvider, AccountStatus> checkFinalBalanceMap(Coin coin, String address) {
        CoinManager manager = coinManagerFactory.getManager(coin);
        return manager.getTransactionStatusMap(address);
    }


}
