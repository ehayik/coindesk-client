package com.github.ehayik.coindesk;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

    @Autowired
    BitcoinPriceIndexClient bpiClient;

    @Test
    void contextLoads() {
        BitcoinCurrentPrice bpi = bpiClient.getPriceIndexOnRealTime();
        System.out.println(bpi.getUsdPrice().getRateFloat());
    }
}
