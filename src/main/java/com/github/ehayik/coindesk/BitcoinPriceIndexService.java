package com.github.ehayik.coindesk;

import java.util.HashMap;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import lombok.RequiredArgsConstructor;
import org.javamoney.moneta.Money;

@RequiredArgsConstructor
class BitcoinPriceIndexService {

    private final BitcoinPriceIndexClient bpiClient;

    PriceIndex getPriceIndexOnRealTime() {
        return asPriceIndex(bpiClient.getPriceIndexOnRealTime());
    }

    private static PriceIndex asPriceIndex(BitcoinCurrentPrice priceIndex) {
        var prices = new HashMap<CurrencyUnit, MonetaryAmount>();
        prices.put(
                Monetary.getCurrency("USD"), Money.of(priceIndex.getUsdPrice().getRateFloat(), "USD"));
        return new PriceIndex(prices);
    }
}
