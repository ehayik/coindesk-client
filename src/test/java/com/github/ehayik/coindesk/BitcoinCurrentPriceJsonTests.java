package com.github.ehayik.coindesk;

import static com.github.ehayik.coindesk.BitcoinPriceIndexJsonFactory.getCurrentPriceIndexJson;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

@JsonTest
class BitcoinCurrentPriceJsonTests {

    @Autowired
    JacksonTester<BitcoinCurrentPrice> jsonUnderTest;

    @Test
    void givenJsonWhenDeserializationThenCorrect() throws IOException {
        // Given
        var jsonContent = getCurrentPriceIndexJson();

        // When
        var result = jsonUnderTest.parse(jsonContent).getObject();

        // Then
        assertThat(result).isNotNull();

        assertThat(result.getUsdPrice()) //
                .isNotNull() //
                .hasFieldOrPropertyWithValue("rateFloat", 27611.6511);

        assertThat(result.getGbpPrice()) //
                .isNotNull() //
                .hasFieldOrPropertyWithValue("rateFloat", 23072.0748);

        assertThat(result.getEurPrice()) //
                .isNotNull() //
                .hasFieldOrPropertyWithValue("rateFloat", 26897.7795);
    }
}
