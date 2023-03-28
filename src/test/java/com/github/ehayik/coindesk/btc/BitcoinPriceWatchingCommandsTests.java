package com.github.ehayik.coindesk.btc;

import static com.github.ehayik.coindesk.btc.BitcoinPriceIndexFactory.*;
import static org.awaitility.Awaitility.await;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.github.ehayik.coindesk.shell.ShellHelper;
import java.time.Duration;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.verification.VerificationMode;
import reactor.core.publisher.Mono;

@MockitoSettings
class BitcoinPriceWatchingCommandsTests {

    @Mock
    ShellHelper shellHelper;

    @Mock
    BitcoinPriceIndexService bitcoinPriceIndexService;

    @InjectMocks
    BitcoinPriceWatchingCommands componentUnderTest;

    @AfterEach
    void tearDown() {
        componentUnderTest.stopBitcoinPriceRequest();
    }

    @ParameterizedTest
    @MethodSource("watchingIntervalSource")
    void watchBitcoinPriceShouldPrintPriceOne(Duration givenInterval) {
        // Given
        given(bitcoinPriceIndexService.getPriceIndexOnRealTime()) //
                .willReturn(Mono.just(createMonetaryAmountList()));

        // When
        componentUnderTest.displayBitcoinPrice(givenInterval);

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

    public static Stream<Duration> watchingIntervalSource() {
        return Stream.of(null, Duration.ZERO);
    }

    @Test
    void watchBitcoinPriceShouldPrintPriceTwice() {
        // Given
        given(bitcoinPriceIndexService.getPriceIndexOnRealTime()) //
                .willReturn(Mono.just(createMonetaryAmountList()));

        // When
        componentUnderTest.displayBitcoinPrice(Duration.ofMillis(3));

        // Then
        await().untilAsserted(() -> assertThatPricesTableIsPrinted(atLeast(2)));
    }
}
