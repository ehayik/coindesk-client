package com.github.ehayik.coindesk.adapter.client;

import static java.time.Duration.ZERO;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;

import com.github.ehayik.coindesk.adapter.in.shell.GetBtcCurrentPriceShell;
import com.github.ehayik.coindesk.adapter.in.shell.ShellHelper;
import com.github.ehayik.coindesk.test.fixtures.MockCoinDeskServer;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock;
import com.maciejwalkowiak.wiremock.spring.EnableWireMock;
import com.maciejwalkowiak.wiremock.spring.WireMock;
import java.time.Duration;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.verification.VerificationMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@EnableWireMock({@ConfigureWireMock(name = "coindesk-server", property = "coindesk-client.url")})
class BtcPriceIndexAdapterTests {

    @MockBean
    private ShellHelper shellHelper;

    @Autowired
    private GetBtcCurrentPriceShell shellCommands;

    @SuppressWarnings("unused")
    @WireMock("coindesk-server")
    private WireMockServer coinDeskServer;

    private MockCoinDeskServer mockCoinDeskServer;

    @BeforeEach
    void setUp() {
        mockCoinDeskServer = new MockCoinDeskServer(coinDeskServer);
    }

    @AfterEach
    void tearDown() {
        shellCommands.stopBitcoinPriceRequest();
    }

    @ParameterizedTest
    @MethodSource("watchingIntervalSource")
    void shouldGetBitcoinCurrentPriceOnlyOnce(Duration givenInterval) {
        // Given
        mockCoinDeskServer.givenBitcoinPriceIndexRequest();

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
        mockCoinDeskServer.givenBitcoinPriceIndexRequest();

        // When
        shellCommands.watchBitcoinPrice(Duration.ofMillis(3));

        // Then
        await().untilAsserted(() -> assertThatPricesTableIsPrinted(atLeast(2)));
    }
}
