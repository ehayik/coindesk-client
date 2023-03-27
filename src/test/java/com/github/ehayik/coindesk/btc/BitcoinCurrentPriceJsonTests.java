package com.github.ehayik.coindesk.btc;

import static com.github.ehayik.coindesk.btc.BitcoinPriceIndexFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.ComponentScan.Filter;

@JsonTest(excludeFilters = @Filter(type = ASSIGNABLE_TYPE, classes = BitcoinPriceIndexClientConfig.class))
class BitcoinCurrentPriceJsonTests {

    @Autowired
    JacksonTester<BitcoinCurrentPrice> jsonUnderTest;

    @Test
    void givenJsonWhenDeserializationThenCorrect() throws IOException {
        // Given
        var jsonContent = createCurrentPriceIndexJson();

        // When
        var result = jsonUnderTest.parse(jsonContent).getObject();

        // Then
        assertThat(result.getPrices())
                .isNotEmpty()
                .contains(createPrice(PRICE_USD), createPrice(PRICE_GBP), createPrice(PRICE_EUR));
    }
}
