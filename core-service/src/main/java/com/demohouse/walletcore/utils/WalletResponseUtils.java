package com.demohouse.walletcore.utils;

import com.demohouse.walletcore.entities.ErrorDTO;
import com.demohouse.walletcore.entities.WalletResponseDTO;

import java.util.Date;

public class WalletResponseUtils {

    public static <Type> WalletResponseDTO<Type> response(Type response) {
        return response(response, null);
    }

    public static <Type> WalletResponseDTO<Type> response(Type response, String message) {
        WalletResponseDTO result = new WalletResponseDTO();
        result.setData(response);
        result.setMessage(message);
        result.setSuccess(true);
        result.setTimestamp(new Date());
        return result;
    }

    public static WalletResponseDTO<ErrorDTO> error(ErrorDTO errorDTO) {
        WalletResponseDTO result = new WalletResponseDTO();
        result.setData(errorDTO);
        result.setMessage(null);
        result.setSuccess(false);
        result.setTimestamp(new Date());
        return result;
    }
}
