package com.github.ehayik.coindesk.application.port.out;

import java.util.List;
import javax.money.MonetaryAmount;
import org.reactivestreams.Publisher;

public interface GetBtcCurrentPricePort {

    Publisher<List<MonetaryAmount>> getBtcCurrentPrice();
}
