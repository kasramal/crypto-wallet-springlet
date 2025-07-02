package com.demohouse.walletcore.services;

import com.demohouse.walletcore.core.transactions.outputs.PaymentDetails;
import com.demohouse.walletcore.entities.Coin;
import com.demohouse.walletcore.services.dto.WalletTransactionDTO;
import com.demohouse.walletcore.services.dto.WalletTransactionInquiryDTO;

public interface WalletTransactionService {

    String signedTransaction(Coin coin, WalletTransactionDTO walletTransactionDTO);

    String pushRawTransaction(Coin coin, WalletTransactionDTO walletTransactionDTO);

    String pushHexTransaction(Coin coin, String transactionHex);

    WalletTransactionInquiryDTO inquiryTransaction(Coin coin, String transactionId);

    PaymentDetails inquiryTransactionByIdAndAddress(Coin coin, String transactionId, String address);
}
