package com.demohouse.walletcore.core.price.coinrankings;

import com.demohouse.walletcore.core.price.CurrencyPriceParser;
import com.demohouse.walletcore.entities.CurrencyPrice;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CoinRankingsCurrencyPriceParser implements CurrencyPriceParser {

    private static final String CURRENCY_NAME_CLASS = "profile__link";
    private static final String CURRENCY_ISO_CLASS = "profile__subtitle";
    private static final String CURRENCY_LOGO_CLASS = "table__logo";
    private static final String CURRENCY_PRICE_CLASS = "valuta valuta--light";
    private static final String CURRENCY_MARKET_CAP_CLASS = "valuta valuta--light";
    private static final String CURRENCY_CHANGE_CLASS = "change change--light";

    @Override
    public CurrencyPrice parseElement(Element element) {
        CurrencyPrice currencyPrice = new CurrencyPrice();

        String name = element.getElementsByClass(CURRENCY_NAME_CLASS).text();
        String iso = element.getElementsByClass(CURRENCY_ISO_CLASS).text();
        String logo = element.getElementsByClass(CURRENCY_LOGO_CLASS).attr("src");
        String changes = element.getElementsByAttributeValueContaining("class", CURRENCY_CHANGE_CLASS).text();

        currencyPrice.setName(name);
        currencyPrice.setIso(iso);
        currencyPrice.setLogoUrl(logo);
        currencyPrice.setChanges24H(changes);

        if (element.getElementsByClass(CURRENCY_PRICE_CLASS).size() > 1) {
            String price = element
                    .getElementsByClass(CURRENCY_PRICE_CLASS)
                    .get(0)
                    .text()
                    .replace("$", "")
                    .replace(",", "")
                    .trim();

            String marketCap = element
                    .getElementsByClass(CURRENCY_PRICE_CLASS)
                    .get(1)
                    .text();

            currencyPrice.setPrice(new BigDecimal(price));
            currencyPrice.setMarketCap(marketCap);
        }

        return currencyPrice;
    }
}
