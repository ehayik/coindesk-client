package com.github.ehayik.coindesk.adapter.client;

import com.github.ehayik.coindesk.core.BitcoinCurrentPriceIndexPort;
import java.util.List;
import javax.money.MonetaryAmount;
import lombok.RequiredArgsConstructor;
import org.javamoney.moneta.Money;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class BitcoinPriceIndexAdapter implements BitcoinCurrentPriceIndexPort {

    private final BitcoinPriceIndexClient priceIndexClient;

    @Override
    public Publisher<List<MonetaryAmount>> getBitcoinCurrentPriceIndex() {
        return priceIndexClient.getPriceIndexOnRealTime().map(BitcoinPriceIndexAdapter::asMonetaryAmountList);
    }

    private static List<MonetaryAmount> asMonetaryAmountList(BitcoinCurrentPrices priceIndex) {
        return priceIndex //
                .getPrices()
                .stream()
                .map(price -> Money.of(price.getRate(), price.getCode()))
                .map(MonetaryAmount.class::cast)
                .toList();
    }
}
