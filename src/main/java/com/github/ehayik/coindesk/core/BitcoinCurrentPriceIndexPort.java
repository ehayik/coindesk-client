package com.github.ehayik.coindesk.core;

import java.util.List;
import javax.money.MonetaryAmount;
import org.reactivestreams.Publisher;

public interface BitcoinCurrentPriceIndexPort {

    Publisher<List<MonetaryAmount>> getBitcoinCurrentPriceIndex();
}
