package com.github.ehayik.coindesk.btc;

import static com.github.ehayik.coindesk.btc.BitcoinPriceIndexFactory.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.list;

import com.github.ehayik.coindesk.btc.BitcoinCurrentPrice.Price;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock;
import com.maciejwalkowiak.wiremock.spring.EnableWireMock;
import com.maciejwalkowiak.wiremock.spring.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableWireMock({@ConfigureWireMock(name = "bpi-service", property = "bpi-client.url")})
class BitcoinPriceIndexClientTests {

    @WireMock("bpi-service")
    WireMockServer wiremock;

    @Autowired
    BitcoinPriceIndexClient bpiClient;

    @Test
    void givenCurrentPriceRequestWhenExchangeThenCorrect() {
        // Given
        wiremock.stubFor(get("/bpi/currentprice.json")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/javascript")
                        .withBody(createCurrentPriceIndexJson())));

        // When
        BitcoinCurrentPrice result =
                bpiClient.getPriceIndexOnRealTime().blockOptional().orElse(null);

        // Then
        assertThat(result)
                .isNotNull()
                .extracting(BitcoinCurrentPrice::getPrices, as(list(Price.class)))
                .isNotEmpty()
                .contains(createPrice(PRICE_USD), createPrice(PRICE_GBP), createPrice(PRICE_EUR));
    }
}
