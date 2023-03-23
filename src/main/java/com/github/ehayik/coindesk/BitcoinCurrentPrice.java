package com.github.ehayik.coindesk;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
class BitcoinCurrentPrice {

    private BitcoinPriceIndex bpi;

    @JsonIgnore
    public Price getUsdPrice() {
        return bpi.getUsdPrice();
    }

    public Price getGbpPrice() {
        return bpi.getGbpPrice();
    }

    public Price getEurPrice() {
        return bpi.getEurPrice();
    }

    @Data
    @JsonRootName(value = "bpi")
    static class BitcoinPriceIndex {

        @JsonProperty("USD")
        private Price usdPrice;

        @JsonProperty("GBP")
        private Price gbpPrice;

        @JsonProperty("EUR")
        private Price eurPrice;
    }

    @Data
    @JsonNaming(SnakeCaseStrategy.class)
    static class Price {
        private Double rateFloat;
    }
}
