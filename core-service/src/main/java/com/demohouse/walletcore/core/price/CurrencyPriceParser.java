package com.demohouse.walletcore.core.price;

import com.demohouse.walletcore.entities.CurrencyPrice;
import org.jsoup.nodes.Element;

public interface CurrencyPriceParser {

    CurrencyPrice parseElement(Element element);
}
