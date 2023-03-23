package com.github.ehayik.coindesk;

import static org.springframework.web.reactive.function.client.support.WebClientAdapter.forClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    BitcoinPriceIndexClient bitcoinPriceIndexClient(WebClient.Builder builder, @Value("${bpi-client.url}") String url) {
        var webClient = builder.baseUrl(url).build();

        var httpServiceProxyFactory =
                HttpServiceProxyFactory.builder(forClient(webClient)).build();

        return httpServiceProxyFactory.createClient(BitcoinPriceIndexClient.class);
    }
}
