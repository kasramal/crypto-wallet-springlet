package com.demohouse.walletcore.core.providers.zcha.helpers;

import com.demohouse.walletcore.core.providers.zcha.api.ZchaClient;
import com.demohouse.walletcore.core.providers.zcha.api.model.response.ZchaAccountBalanceResponse;
import com.demohouse.walletcore.core.providers.zcha.api.model.response.ZchaInputResponse;
import com.demohouse.walletcore.core.providers.zcha.api.model.response.ZchaNetworkResponse;
import com.demohouse.walletcore.core.providers.zcha.api.model.response.ZchaTransactionResponse;
import com.demohouse.walletcore.core.transactions.CryptoCurrencyPayment;
import com.demohouse.walletcore.core.transactions.outputs.PaymentDetails;
import com.demohouse.walletcore.core.transactions.services.AccountStatus;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyError;
import com.demohouse.walletcore.entities.Coin;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ZcashHelper implements ZchaHelper {

    private final ZchaClient zchaClient;

    public ZcashHelper(ZchaClient zchaClient) {
        this.zchaClient = zchaClient;
    }

    @Override
    public Coin getCoin() {
        return Coin.ZCASH;
    }

    @Override
    public String createTransaction(String privateKey, String from, List<CryptoCurrencyPayment> payments, BigDecimal fee) {
        throw new CryptoCurrencyApiException(CryptoCurrencyError.API_ERROR);
    }

    @Override
    public BigDecimal getAddressBalance(String address) {
        ZchaAccountBalanceResponse accountBalance = zchaClient.getAccountBalance(address);
        return this.processValue(accountBalance.getBalance());
    }

    @Override
    public Boolean isConfirmed(String address) {
        ZchaNetworkResponse network = zchaClient.getNetwork();
        List<ZchaTransactionResponse> transactionByAddress = zchaClient.getTransactionByAddress(address);
        for (ZchaTransactionResponse byAddress : transactionByAddress) {
            long confirmation = 1 + (network.getBlockNumber() - byAddress.getBlockHeight());
            if (confirmation < 1) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Boolean isConfirmedByTransactionId(String transactionId) {
        ZchaNetworkResponse network = zchaClient.getNetwork();
        ZchaTransactionResponse transactionByHash = this.zchaClient.getTransactionByHash(transactionId);
        long confirmation = 1 + (network.getBlockNumber() - transactionByHash.getBlockHeight());
        return confirmation >= 1;
    }

    @Override
    public AccountStatus getTransactionStatus(String address) {
        ZchaNetworkResponse network = zchaClient.getNetwork();
        List<ZchaTransactionResponse> transactionByAddress = zchaClient.getTransactionByAddress(address);
        boolean isConfirmed = true;
        String transactionId = null;
        BigDecimal unconfirmedBalance = BigDecimal.ZERO;
        for (ZchaTransactionResponse transaction : transactionByAddress) {
            long confirmation = 1 + (network.getBlockNumber() - transaction.getBlockHeight());
            if (confirmation >= 1) {
                isConfirmed = true;
                transactionId = transaction.getHash();
            } else {

                isConfirmed = false;
                for (ZchaInputResponse inputResponse : transaction.getInputs()) {
                    if (inputResponse.getAddresses().contains(address)) {
                        unconfirmedBalance.subtract(inputResponse.getValue());
                    }
                }
                unconfirmedBalance = unconfirmedBalance.add(transaction.getOutputValue());
            }
        }
        ZchaAccountBalanceResponse accountBalance = zchaClient.getAccountBalance(address);
        BigDecimal balance = processValue(accountBalance.getBalance());
        unconfirmedBalance = processValue(unconfirmedBalance);
        return new AccountStatus(isConfirmed, unconfirmedBalance, balance, transactionId);
    }

    @Override
    public PaymentDetails getTransactionPaymentDetails(String txHash, String address) {
        ZchaTransactionResponse transactionByHash = zchaClient.getTransactionByHash(txHash);
        validatePaymentDetails(transactionByHash);
        String buyerAddress = null;
        for (ZchaInputResponse input : transactionByHash.getInputs()) {
            buyerAddress = input.getAddresses().get(0);
        }
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setReceivedValue(this.processValue(transactionByHash.getValue()));
        paymentDetails.setSpentValue(this.processValue(transactionByHash.getOutputValue()));
        paymentDetails.setTxId(txHash);
        paymentDetails.setFee(this.processValue(transactionByHash.getFee()));
        paymentDetails.setBuyerAddress(buyerAddress);
        return paymentDetails;
    }

    @Override
    public BigDecimal processValue(BigDecimal value) {
        return value;
    }
}
