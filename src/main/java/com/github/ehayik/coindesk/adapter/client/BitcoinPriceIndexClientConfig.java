package com.github.ehayik.coindesk.adapter.client;

import static org.springframework.util.MimeTypeUtils.parseMimeType;
import static org.springframework.web.reactive.function.client.support.WebClientAdapter.forClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
class BitcoinPriceIndexClientConfig {

    @Bean
    @SuppressWarnings("unused")
    BitcoinPriceIndexClient bitcoinPriceIndexClient(
            WebClient.Builder builder, @Value("${bpi-client.url}") String url, ObjectMapper objectMapper) {
        var customDecoder = new Jackson2JsonDecoder(objectMapper, parseMimeType("application/javascript"));

        var webClient = builder.baseUrl(url)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(x -> x.customCodecs().registerWithDefaultConfig(customDecoder))
                        .build())
                .build();

        var httpServiceProxyFactory =
                HttpServiceProxyFactory.builder(forClient(webClient)).build();

        return httpServiceProxyFactory.createClient(BitcoinPriceIndexClient.class);
    }
}
