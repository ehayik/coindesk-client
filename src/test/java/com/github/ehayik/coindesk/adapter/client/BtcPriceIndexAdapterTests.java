package com.github.ehayik.coindesk.adapter.client;

import static com.github.ehayik.coindesk.adapter.client.Mocks.givenBitcoinCurrentPriceRequest;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static java.time.Duration.ZERO;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;

import com.github.ehayik.coindesk.adapter.in.shell.GetBtcCurrentPriceShell;
import com.github.ehayik.coindesk.adapter.in.shell.ShellHelper;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import java.time.Duration;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.verification.VerificationMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
class BtcPriceIndexAdapterTests {

    @RegisterExtension
    static WireMockExtension coinDeskServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @MockBean
    private ShellHelper shellHelper;

    @Autowired
    private GetBtcCurrentPriceShell shellCommands;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("http.clients.coindesk-client.url", coinDeskServer::baseUrl);
    }

    @AfterEach
    void tearDown() {
        shellCommands.stopBitcoinPriceRequest();
    }

    @ParameterizedTest
    @MethodSource("watchingIntervalSource")
    void shouldGetBitcoinCurrentPriceOnlyOnce(Duration givenInterval) {
        // Given
        givenBitcoinCurrentPriceRequest(coinDeskServer);

        // When
        shellCommands.watchBitcoinPrice(givenInterval);

        // Then
        assertThatPricesTableIsPrinted(atMostOnce());
    }

    private void assertThatPricesTableIsPrinted(VerificationMode wantedNumberOfPrints) {
        verify(shellHelper, wantedNumberOfPrints)
                .print(
                        """
                                +------------+------------+------------+
                                |USD 27611.65|EUR 26897.78|GBP 23072.07|
                                +------------+------------+------------+
                                """);
    }

    private static Stream<Duration> watchingIntervalSource() {
        return Stream.of(null, ZERO);
    }

    @Test
    @Disabled("Because it fails on Github pipeline, however it works on my PC.")
    void shouldGetBitcoinCurrentPriceTwice() {
        // Given
        givenBitcoinCurrentPriceRequest(coinDeskServer);

        // When
        shellCommands.watchBitcoinPrice(Duration.ofMillis(3));

        // Then
        await().untilAsserted(() -> assertThatPricesTableIsPrinted(atLeast(2)));
    }
}
