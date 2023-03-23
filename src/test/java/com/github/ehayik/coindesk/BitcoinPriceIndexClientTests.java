package com.github.ehayik.coindesk;

import static com.github.ehayik.coindesk.BitcoinPriceIndexJsonFactory.getCurrentPriceIndexJson;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static org.assertj.core.api.Assertions.assertThat;

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
    private WireMockServer wiremock;

    @Autowired
    BitcoinPriceIndexClient bpiClient;

    @Test
    void givenCurrentPriceRequestWhenExchangeThenCorrect() {
        // Given
        wiremock.stubFor(get("/bpi/currentprice.json")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/javascript")
                        .withBody(getCurrentPriceIndexJson())));

        // When
        BitcoinCurrentPrice priveIndex = bpiClient.getPriceIndexOnRealTime();

        // Then
        assertThat(priveIndex).isNotNull();
    }
}
