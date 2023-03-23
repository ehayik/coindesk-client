package com.github.ehayik.coindesk;

import java.util.Map;
import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;

record PriceIndex(Map<CurrencyUnit, MonetaryAmount> prices) {}
