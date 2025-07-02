package com.demohouse.walletcore.controller;

import com.demohouse.walletcore.controller.vm.HDWalletVM;
import com.demohouse.walletcore.core.hdwallets.HDWallet;
import com.demohouse.walletcore.core.hdwallets.HDWalletCredentials;
import com.demohouse.walletcore.entities.Coin;
import com.demohouse.walletcore.entities.WalletResponseDTO;
import com.demohouse.walletcore.services.WalletService;
import com.demohouse.walletcore.services.dto.WalletBalanceDTO;
import com.demohouse.walletcore.utils.WalletResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/hd-wallet/{coin}")
public class HDWalletController extends WalletBaseController {

    private final WalletService walletService;

    public HDWalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/mnemonic")
    public ResponseEntity<WalletResponseDTO<HDWalletVM>> generateMnemonic(Authentication authentication) {
        final Object credentials = authentication.getCredentials();
        String mnemonic = this.walletService.generateMnemonic();
        return ResponseEntity.ok(
                WalletResponseUtils.response(new HDWalletVM()
                .setMnemonic(mnemonic)));
    }

    @PostMapping("/generate")
    public ResponseEntity<WalletResponseDTO<HDWalletVM>> generateWallet(@PathVariable("coin") String coinIso, @RequestBody HDWalletCredentials credentials) {
        Coin byIso = Coin.findByIso(coinIso);
        HDWallet hdWallet = this.walletService.generateHDWallet(byIso, credentials.getPassword());
        return ResponseEntity.ok(
                WalletResponseUtils.response(new HDWalletVM()
                .setMnemonic(hdWallet.getCredentials().getMnemonic())
                .setAddress(hdWallet.getHdWalletAddress().getAddress())
                .setPublicKey(hdWallet.getHdWalletAddress().getPublicKey())
                .setXPublicKey(hdWallet.getHdWalletAddress().getXPublicKey())));
    }

    @PostMapping("/retrieve")
    public ResponseEntity<WalletResponseDTO<HDWalletVM>> retrieveWallet(@PathVariable("coin") String coinIso, @RequestBody HDWalletCredentials credentials) {
        Coin byIso = Coin.findByIso(coinIso);
        HDWallet hdWallet = this.walletService.retrieveHDWallet(byIso, credentials.getMnemonic(), credentials.getPassword());
        return ResponseEntity.ok(
                WalletResponseUtils.response(new HDWalletVM()
                .setMnemonic(hdWallet.getCredentials().getMnemonic())
                .setAddress(hdWallet.getHdWalletAddress().getAddress())
                .setPublicKey(hdWallet.getHdWalletAddress().getPublicKey())
                .setXPublicKey(hdWallet.getHdWalletAddress().getXPublicKey())));
    }

    @PostMapping("/balance")
    public ResponseEntity<WalletResponseDTO<WalletBalanceDTO>> checkBalance(@PathVariable("coin") String coinIso, @RequestBody HDWalletCredentials credentials) {
        Coin byIso = Coin.findByIso(coinIso);
        String address = this.walletService.retrieveHDWallet(byIso, credentials.getMnemonic(), credentials.getPassword()).getHdWalletAddress().getAddress();
        WalletBalanceDTO walletBalanceDTO = this.walletService.checkBalance(byIso, address);
        return ResponseEntity.ok(
                WalletResponseUtils.response(walletBalanceDTO));
    }

}
