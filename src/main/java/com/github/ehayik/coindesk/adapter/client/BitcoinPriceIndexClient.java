package com.github.ehayik.coindesk.adapter.client;

import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import reactor.core.publisher.Mono;

@HttpExchange(url = "/bpi")
public interface BitcoinPriceIndexClient {

    @GetExchange(value = "/currentprice.json")
    Mono<BitcoinCurrentPrices> getPriceIndexOnRealTime();
}
