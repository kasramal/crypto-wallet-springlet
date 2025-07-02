package com.demohouse.walletcore.controller;

import com.demohouse.walletcore.controller.vm.WalletVM;
import com.demohouse.walletcore.core.providers.CryptoCurrencyApiProvider;
import com.demohouse.walletcore.core.transactions.services.AccountStatus;
import com.demohouse.walletcore.entities.Coin;
import com.demohouse.walletcore.entities.WalletResponseDTO;
import com.demohouse.walletcore.services.WalletService;
import com.demohouse.walletcore.services.dto.WalletBalanceDTO;
import com.demohouse.walletcore.utils.WalletResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/v1/wallet/{coin}")
public class WalletController extends WalletBaseController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/generate")
    public ResponseEntity<WalletResponseDTO<WalletVM>> generateWallet(@PathVariable("coin") String coinIso) {
        Coin byIso = Coin.findByIso(coinIso);
        String generatedWallet = this.walletService.generateWallet(byIso);
        return ResponseEntity.ok(
                WalletResponseUtils.response(new WalletVM(generatedWallet)));
    }

    @GetMapping("/balance/{address}")
    public ResponseEntity<WalletResponseDTO<WalletBalanceDTO>> checkBalance(@PathVariable("coin") String coinIso, @PathVariable("address") String address) {
        Coin byIso = Coin.findByIso(coinIso);
        WalletBalanceDTO walletBalanceDTO = this.walletService.checkBalance(byIso, address);
        return ResponseEntity.ok(
                WalletResponseUtils.response(walletBalanceDTO));
    }

    @GetMapping("/balance/final/{address}")
    public ResponseEntity<WalletResponseDTO<AccountStatus>> checkFinalBalance(@PathVariable("coin") String coinIso, @PathVariable("address") String address) {
        Coin byIso = Coin.findByIso(coinIso);
        AccountStatus walletBalanceDTO = this.walletService.checkFinalBalance(byIso, address);
        return ResponseEntity.ok(
                WalletResponseUtils.response(walletBalanceDTO));
    }

    @GetMapping("/balance/final/{address}/map")
    public ResponseEntity<WalletResponseDTO<Map<CryptoCurrencyApiProvider, AccountStatus>>> checkFinalBalanceMap(@PathVariable("coin") String coinIso, @PathVariable("address") String address) {
        Coin byIso = Coin.findByIso(coinIso);
        Map<CryptoCurrencyApiProvider, AccountStatus>
                cryptoCurrencyApiProviderCryptoCurrencyTransactionStatusMap = this.walletService.checkFinalBalanceMap(byIso, address);
        return ResponseEntity.ok(
                WalletResponseUtils.response(cryptoCurrencyApiProviderCryptoCurrencyTransactionStatusMap));
    }

}
