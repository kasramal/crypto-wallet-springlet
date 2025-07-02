package com.demohouse.walletcore.core.manager.util;

import com.demohouse.walletcore.core.providers.CryptoCurrencyApiService;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyApiException;
import com.demohouse.walletcore.core.transactions.services.CryptoCurrencyError;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.concurrent.Callable;

public class RunnerUtils {

    public static void handleExceptionAndLockApiService(CryptoCurrencyApiService service, WebClientResponseException e) {
        if (HttpStatus.TOO_MANY_REQUESTS.equals(e.getStatusCode())
                || HttpStatus.UNAUTHORIZED.equals(e.getStatusCode())
                || HttpStatus.FORBIDDEN.equals(e.getStatusCode())) {
            service.lock();
            System.out.println(service.getApiProvider() + " Locked due to " + e.getStatusCode());
        }
    }

    public static <RESULT> RESULT pushApiCallRunner(CryptoCurrencyApiService service, Callable<RESULT> pusher) {
        try {
            return pusher.call();
        } catch (WebClientResponseException e) {
            if (HttpStatus.BAD_REQUEST.equals(e.getStatusCode()))
                throw new CryptoCurrencyApiException(CryptoCurrencyError.INVALID_TRANSACTION);
            handleExceptionAndLockApiService(service, e);
        } catch (CryptoCurrencyApiException apiException) {
            if (!apiException.getError().equals(CryptoCurrencyError.API_DOES_NOT_SUPPORT_PUSH_TX))
                throw apiException;
        } catch (Throwable ignored) {
            ignored.printStackTrace();
        }
        return null;
    }

    public static <Result> Result apiCallRunner(CryptoCurrencyApiService service, Callable<Result> pusher) {
        try {
            return pusher.call();
        } catch (WebClientResponseException e) {
            handleExceptionAndLockApiService(service, e);
        } catch (CryptoCurrencyApiException apiException) {
            throw apiException;
        } catch (Throwable ignored) {
            ignored.printStackTrace();
        }
        return null;
    }
}
