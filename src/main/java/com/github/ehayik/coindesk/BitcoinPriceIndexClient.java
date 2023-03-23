package com.github.ehayik.coindesk;

import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(url = "/bpi")
public interface BitcoinPriceIndexClient {

    @GetExchange(value = "/currentprice.json")
    BitcoinCurrentPrice getPriceIndexOnRealTime();
}
