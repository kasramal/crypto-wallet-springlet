package com.demohouse.walletcore.controller;


import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.entities.ErrorDTO;
import com.demohouse.walletcore.entities.WalletResponseDTO;
import com.demohouse.walletcore.utils.WalletResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

public class WalletBaseController {
    @Autowired
    @Qualifier("walletMessageSource")
    private MessageSource resource;

    protected String getErrorMessage(String key) {
        return resource.getMessage(key, null, LocaleContextHolder.getLocale());
    }

    protected String getErrorMessage(String key, Object... args) {
        return resource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    @ExceptionHandler({CryptoCurrencyApiException.class})
    public WalletResponseDTO<ErrorDTO> handleMethodArgumentTypeMismatch(
            CryptoCurrencyApiException ex, WebRequest request) {
        String message = ex.getArgs() != null ? getErrorMessage(ex.getError().getMessageKey(), ex.getArgs()) : getErrorMessage(ex.getError().getMessageKey());
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage(message);
        errorDTO.setCode((long) ex.getError().ordinal());
        errorDTO.setException(ex.getError().getName());
        return WalletResponseUtils.error(errorDTO);
    }

}
