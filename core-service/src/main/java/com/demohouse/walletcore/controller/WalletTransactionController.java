package com.demohouse.walletcore.controller;


import com.demohouse.walletcore.controller.vm.WalletPushHexTransactionVM;
import com.demohouse.walletcore.controller.vm.WalletSignTransactionVM;
import com.demohouse.walletcore.controller.vm.WalletTransactionVM;
import com.demohouse.walletcore.core.transactions.outputs.PaymentDetails;
import com.demohouse.walletcore.entities.Coin;
import com.demohouse.walletcore.entities.WalletResponseDTO;
import com.demohouse.walletcore.services.WalletTransactionService;
import com.demohouse.walletcore.services.dto.WalletTransactionDTO;
import com.demohouse.walletcore.services.dto.WalletTransactionInquiryDTO;
import com.demohouse.walletcore.utils.WalletResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/wallet-transaction/{coin}")
public class WalletTransactionController extends WalletBaseController {

    private final WalletTransactionService walletTransactionService;

    public WalletTransactionController(WalletTransactionService walletTransactionService) {
        this.walletTransactionService = walletTransactionService;
    }

    @PostMapping("/sign")
    public ResponseEntity<WalletResponseDTO<WalletSignTransactionVM>> signTransaction(@PathVariable("coin") String coinIso, @RequestBody @Valid WalletTransactionDTO walletTransactionDTO) {
        Coin byIso = Coin.findByIso(coinIso);
        String signedTransaction = this.walletTransactionService.signedTransaction(byIso, walletTransactionDTO);
        return ResponseEntity.ok(
                WalletResponseUtils.response(new WalletSignTransactionVM(signedTransaction)));
    }

    @PostMapping("/push/raw")
    public ResponseEntity<WalletResponseDTO<WalletTransactionVM>> pushRawTransaction(@PathVariable("coin") String coinIso, @RequestBody @Valid WalletTransactionDTO walletTransactionDTO) {
        Coin byIso = Coin.findByIso(coinIso);
        String transactionId = this.walletTransactionService.pushRawTransaction(byIso, walletTransactionDTO);
        return ResponseEntity.ok(
                WalletResponseUtils.response(new WalletTransactionVM(transactionId)));
    }

    @PostMapping("/push/hex")
    public ResponseEntity<WalletResponseDTO<WalletTransactionVM>> pushHexTransaction(@PathVariable("coin") String coinIso, @RequestBody @Valid WalletPushHexTransactionVM pushHexTransactionVM) {
        Coin byIso = Coin.findByIso(coinIso);
        String transactionId = this.walletTransactionService.pushHexTransaction(byIso, pushHexTransactionVM.getTransactionHex());
        return ResponseEntity.ok(
                WalletResponseUtils.response(new WalletTransactionVM(transactionId)));
    }

    @GetMapping("/inquiry/{txId}")
    public ResponseEntity<WalletResponseDTO<WalletTransactionInquiryDTO>> inquiryTransaction(@PathVariable("coin") String coinIso, @PathVariable("txId") String transactionId) {
        Coin byIso = Coin.findByIso(coinIso);
        WalletTransactionInquiryDTO result = this.walletTransactionService.inquiryTransaction(byIso, transactionId);
        return ResponseEntity.ok(
                WalletResponseUtils.response(result));
    }

    @GetMapping("/inquiry/{address}/{txId}")
    public ResponseEntity<WalletResponseDTO<PaymentDetails>> inquiryTransactionByIdAndAddress(@PathVariable("coin") String coinIso, @PathVariable("address") String address,
                                                                                              @PathVariable("txId") String transactionId) {
        Coin byIso = Coin.findByIso(coinIso);
        PaymentDetails paymentDetails = this.walletTransactionService.inquiryTransactionByIdAndAddress(byIso, transactionId, address);
        return ResponseEntity.ok(
                WalletResponseUtils.response(paymentDetails));
    }
}
