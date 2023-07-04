package com.github.ehayik.coindesk.application.out;

import java.util.List;
import javax.money.MonetaryAmount;
import org.reactivestreams.Publisher;

public interface BtcCurrentPriceIndexPort {

    Publisher<List<MonetaryAmount>> getBitcoinCurrentPriceIndex();
}
