package com.demohouse.walletcore.core.transactions.coins.ethereum.erc20;

import com.demohouse.walletcore.core.transactions.coins.ethereum.EthereumUtils;
import com.demohouse.walletcore.core.transactions.coins.ethereum.erc20.tether.TetherUtils;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyError;
import com.demohouse.walletcore.entities.Coin;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;

public class Erc20TransactionBuilder {


    private static BigDecimal convertErc20Value(Coin coin, BigDecimal value) {
        switch (coin) {
            case TETHER:
                return TetherUtils.convertUSDTToTether(value);
            case BITCOIN:
                return value;
        }
        return value;
    }

    public static Erc20Transaction build(Coin coin, Erc20Address erc20Address, String toAddress, BigDecimal value, BigDecimal fee) {
        Erc20Transaction tetherTransaction = new Erc20Transaction();

        BigDecimal gasPrice = fee.divide(EthereumUtils.MIN_TETHER_GAS_LIMIT, 14, RoundingMode.HALF_UP);
        BigDecimal txValue = convertErc20Value(coin, value);

        Erc20Token tokenToBeSpent = null;
        for (Erc20Token token : erc20Address.getTokens()) {
            if (token.getTokenSymbol().equals(coin.getIso()) && token.getBalance().compareTo(value) >= 0){
                tokenToBeSpent = token;
                if(!token.getTokenAddress().equals(coin.getContractId()))
                    throw new CryptoCurrencyApiException(CryptoCurrencyError.INVALID_CONTRACT_ID);
            }
        }

        if (tokenToBeSpent == null)
            throw new CryptoCurrencyApiException(CryptoCurrencyError.INSUFFICIENT_BALANCE);
        if (value.compareTo(tokenToBeSpent.getBalance()) > 0)
            throw new CryptoCurrencyApiException(CryptoCurrencyError.INSUFFICIENT_BALANCE);
        if (erc20Address.getBalance().compareTo(fee) < 0)
            throw new CryptoCurrencyApiException(CryptoCurrencyError.INSUFFICIENT_GAS_FOR_ERC20_TRANSACTION, new String[]{erc20Address.getBalance().toString(), fee.toString()});

        tetherTransaction.setAmount(txValue);
        tetherTransaction.setNonce(erc20Address.getNonce());
        tetherTransaction.setContractId(tokenToBeSpent.getTokenAddress());
        tetherTransaction.setGasPrice(gasPrice);

        Function function = transfer(toAddress, txValue.toBigInteger());
        String encodedFunction = FunctionEncoder.encode(function);
        tetherTransaction.setEncodedData(encodedFunction);
        BigDecimal gasLimit = EthereumUtils.MIN_TETHER_GAS_LIMIT;
        tetherTransaction.setGasLimit(gasLimit);
        tetherTransaction.setFee(fee);

        return tetherTransaction;
    }


    private static Function transfer(String toAddress, BigInteger value) {
        return new Function(
                "transfer",
                Arrays.asList(new Address(toAddress), new Uint256(value)),
                Collections.singletonList(new TypeReference<Bool>() {
                }));
    }

}
