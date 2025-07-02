package com.demohouse.walletcore.services;

import com.demohouse.walletcore.core.price.CurrencyPriceParser;
import com.demohouse.walletcore.entities.CurrencyPrice;
import com.demohouse.walletcore.repositories.CurrencyPriceRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CurrencyPriceService {

    private final CurrencyPriceRepository currencyPriceRepository;

    private final CurrencyPriceParser currencyPriceParser;

    public CurrencyPriceService(CurrencyPriceRepository currencyPriceRepository, CurrencyPriceParser currencyPriceParser) {
        this.currencyPriceRepository = currencyPriceRepository;
        this.currencyPriceParser = currencyPriceParser;
    }

    @PostConstruct
    public void init() {

        updateCurrencyPrices();
    }

    public CurrencyPrice updateCurrencyPrice(CurrencyPrice currencyPrice) {
        CurrencyPrice oldCP = this.currencyPriceRepository.findByIso(currencyPrice.getIso());
        if (oldCP != null) {
            currencyPrice.setId(oldCP.getId());
        }
        return currencyPrice;
    }

    public void fetchCurrencyPrices(int page) {

        String blogUrl = "https://www.coinranking.com/?page=" + page;
        try {
            Document doc = Jsoup.connect(blogUrl).get();
            Elements coinRows = doc.select("tr.table__row");
            List<CurrencyPrice> currencyPrices = new ArrayList<>();
            coinRows.forEach((coinRow) -> {
                CurrencyPrice currencyPrice = currencyPriceParser.parseElement(coinRow);
                updateCurrencyPrice(currencyPrice);
                currencyPrices.add(currencyPrice);
            });
            currencyPriceRepository.saveAll(currencyPrices);
        } catch (IOException ioException) {
//            ioException.printStackTrace();
        }
    }

    @Scheduled(cron = "28 3/5 * * * ?")
    void updateCurrencyPrices() {
        fetchCurrencyPrices(1);
        fetchCurrencyPrices(2);
    }

    public BigDecimal getPrice(String iso) {
        CurrencyPrice currencyPrice = this.currencyPriceRepository.findByIso(iso);
        if (currencyPrice != null) {
            return currencyPrice.getPrice();
        }
        return new BigDecimal("1.00000");
    }
}
