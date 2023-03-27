package com.github.ehayik.coindesk.btc;

import java.util.List;
import javax.money.MonetaryAmount;
import lombok.RequiredArgsConstructor;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
class BitcoinPriceIndexService {

    private final BitcoinPriceIndexClient bpiClient;

    Mono<List<MonetaryAmount>> getPriceIndexOnRealTime() {
        return bpiClient.getPriceIndexOnRealTime().map(BitcoinPriceIndexService::asMonetaryAmountList);
    }

    private static List<MonetaryAmount> asMonetaryAmountList(BitcoinCurrentPrice priceIndex) {
        return priceIndex //
                .getPrices()
                .stream()
                .map(price -> Money.of(price.getRate(), price.getCode()))
                .map(MonetaryAmount.class::cast)
                .toList();
    }
}
