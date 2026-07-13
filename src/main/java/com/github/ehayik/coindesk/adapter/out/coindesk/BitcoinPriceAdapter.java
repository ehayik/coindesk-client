package com.github.ehayik.coindesk.adapter.out.coindesk;

import com.github.ehayik.coindesk.application.port.out.ForLoadingBitcoinPrice;
import java.util.List;
import javax.money.MonetaryAmount;
import lombok.RequiredArgsConstructor;
import org.javamoney.moneta.Money;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class BitcoinPriceAdapter implements ForLoadingBitcoinPrice {

    private final CoinDeskClient priceIndexClient;

    @Override
    public Publisher<List<MonetaryAmount>> loadBitcoinCurrentPrice() {
        return priceIndexClient.getBtcCurrentPrice().map(BitcoinPriceAdapter::asMonetaryAmountList);
    }

    private static List<MonetaryAmount> asMonetaryAmountList(BtcCurrentPrices priceIndex) {
        return priceIndex //
                .getPrices()
                .stream()
                .map(price -> Money.of(price.getRate(), price.getCode()))
                .map(MonetaryAmount.class::cast)
                .toList();
    }
}
