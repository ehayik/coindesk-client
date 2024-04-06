package com.github.ehayik.coindesk.adapter.out.coindesk;

import com.maciejwalkowiak.spring.http.annotation.HttpClient;
import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Mono;

@HttpClient("coindesk-client")
interface CoinDeskClient {

    @GetExchange(value = "/v1/bpi/currentprice.json")
    Mono<BtcCurrentPrices> getBtcCurrentPrice();
}
