package com.github.ehayik.coindesk.btc;

import static com.github.ehayik.coindesk.btc.BitcoinPriceIndexFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static reactor.core.publisher.Mono.just;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

@MockitoSettings
class BitcoinPriceIndexServiceTest {

    @Mock
    BitcoinPriceIndexClient bpiClient;

    @InjectMocks
    BitcoinPriceIndexService serviceUnderTest;

    @Test
    void getPriceIndexOnRealTimeShouldReturnExpectedValues() {
        // Given
        var givenPriceIndex = createCurrentPriceIndex();
        given(bpiClient.getPriceIndexOnRealTime()).willReturn(just(givenPriceIndex));

        // When
        var result = serviceUnderTest //
                .getPriceIndexOnRealTime()
                .blockOptional()
                .orElseGet(List::of);

        // Then
        assertThat(result) //
                .isNotEmpty()
                .contains(PRICE_USD)
                .contains(PRICE_EUR)
                .contains(PRICE_GBP);
    }
}
