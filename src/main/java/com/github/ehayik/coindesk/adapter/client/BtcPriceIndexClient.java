package com.github.ehayik.coindesk.adapter.client;

import com.maciejwalkowiak.spring.http.annotation.HttpClient;
import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Mono;

@HttpClient("bpi-client")
public interface BtcPriceIndexClient {

    @GetExchange(value = "/currentprice.json")
    Mono<BtcCurrentPrices> getPriceIndexOnRealTime();
}
