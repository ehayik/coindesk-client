package com.github.ehayik.coindesk.application.domain;

import java.util.List;
import java.util.stream.Stream;
import javax.money.MonetaryAmount;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class PriceIndex {

    @NonNull
    private List<MonetaryAmount> prices;

    public Stream<MonetaryAmount> getPrices() {
        return prices.stream();
    }

    @Override
    public String toString() {
        return prices.toString();
    }
}
