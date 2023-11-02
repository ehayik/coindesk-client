package com.github.ehayik.coindesk.adapter.client;

import static org.springframework.util.MimeTypeUtils.parseMimeType;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
class JsonDecoderWebClientCustomizer implements WebClientCustomizer {

    private final ObjectMapper objectMapper;

    @Override
    public void customize(WebClient.Builder webClientBuilder) {
        var customDecoder = new Jackson2JsonDecoder(objectMapper, parseMimeType("application/javascript"));
        webClientBuilder.exchangeStrategies(ExchangeStrategies.builder()
                .codecs(x -> x.customCodecs().registerWithDefaultConfig(customDecoder))
                .build());
    }
}
