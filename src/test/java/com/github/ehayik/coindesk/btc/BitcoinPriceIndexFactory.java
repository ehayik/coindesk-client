package com.github.ehayik.coindesk.btc;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ehayik.coindesk.btc.BitcoinCurrentPrice.Price;
import java.util.List;
import javax.money.MonetaryAmount;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.javamoney.moneta.Money;

@NoArgsConstructor(access = PRIVATE)
final class BitcoinPriceIndexFactory {

    static final MonetaryAmount PRICE_USD = Money.of(27611.6511, "USD");
    static final MonetaryAmount PRICE_EUR = Money.of(26897.7795, "EUR");
    static final MonetaryAmount PRICE_GBP = Money.of(23072.0748, "GBP");

    static String createCurrentPriceIndexJson() {
        return """
                {
                  "time": {
                    "updated": "Mar 20, 2023 16:56:00 UTC",
                    "updatedISO": "2023-03-20T16:56:00+00:00",
                    "updateduk": "Mar 20, 2023 at 16:56 GMT"
                  },
                  "disclaimer": "This data was produced from the CoinDesk Bitcoin Price Index (USD). Non-USD currency data converted using hourly conversion rate from openexchangerates.org",
                  "chartName": "Bitcoin",
                  "bpi": {
                    "USD": {
                      "code": "USD",
                      "symbol": "&#36;",
                      "rate": "27,611.6511",
                      "description": "United States Dollar",
                      "rate_float": 27611.6511
                    },
                    "GBP": {
                      "code": "GBP",
                      "symbol": "&pound;",
                      "rate": "23,072.0748",
                      "description": "British Pound Sterling",
                      "rate_float": 23072.0748
                    },
                    "EUR": {
                      "code": "EUR",
                      "symbol": "&euro;",
                      "rate": "26,897.7795",
                      "description": "Euro",
                      "rate_float": 26897.7795
                    }
                  }
                }
                """;
    }

    @SneakyThrows
    static BitcoinCurrentPrice createCurrentPriceIndex() {
        return new ObjectMapper()
                .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
                .readValue(createCurrentPriceIndexJson(), BitcoinCurrentPrice.class);
    }

    static Price createPrice(@NonNull MonetaryAmount amount) {
        return new Price()
                .setCode(amount.getCurrency().getCurrencyCode())
                .setRate(amount.getNumber().doubleValue());
    }

    static List<MonetaryAmount> createMonetaryAmountList() {
        return List.of(PRICE_USD, PRICE_EUR, PRICE_GBP);
    }
}
