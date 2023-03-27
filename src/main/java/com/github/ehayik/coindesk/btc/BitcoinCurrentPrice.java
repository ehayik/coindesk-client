package com.github.ehayik.coindesk.btc;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import java.util.List;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
public class BitcoinCurrentPrice {

    private BitcoinPriceIndex bpi;

    public List<Price> getPrices() {
        return List.of(bpi.usdPrice, bpi.eurPrice, bpi.gbpPrice);
    }

    @SuppressWarnings("unused")
    @JsonRootName(value = "bpi")
    public static class BitcoinPriceIndex {

        @JsonProperty("USD")
        private Price usdPrice;

        @JsonProperty("GBP")
        private Price gbpPrice;

        @JsonProperty("EUR")
        private Price eurPrice;
    }

    @Data
    @Accessors(chain = true)
    public static class Price {

        private String code;

        @JsonProperty("rate_float")
        private Double rate;
    }
}
