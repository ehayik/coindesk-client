package com.github.ehayik.coindesk.adapter.client;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static lombok.AccessLevel.PRIVATE;

import com.github.tomakehurst.wiremock.junit.Stubbing;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
class Mocks {

    private static final String BTC_REQUEST_RESPONSE =
            """
                    {
                      "time": {
                        "updated": "Mar 20, 2023 16:56:00 UTC",
                        "updatedISO": "2023-03-20T16:56:00+00:00",
                        "updateduk": "Mar 20, 2023 at 16:56 GMT"
                      },
                      "disclaimer": "This data was produced from the CoinDesk Bitcoin Price Index (USD). Non-USD currency data converted using hourly conversion rate from openexchangerates.org",
                      "chartName": "Bitcoin",
                      "bpi": {
                        "USD": {
                          "code": "USD",
                          "symbol": "&#36;",
                          "rate": "27,611.6511",
                          "description": "United States Dollar",
                          "rate_float": 27611.6511
                        },
                        "GBP": {
                          "code": "GBP",
                          "symbol": "&pound;",
                          "rate": "23,072.0748",
                          "description": "British Pound Sterling",
                          "rate_float": 23072.0748
                        },
                        "EUR": {
                          "code": "EUR",
                          "symbol": "&euro;",
                          "rate": "26,897.7795",
                          "description": "Euro",
                          "rate_float": 26897.7795
                        }
                      }
                    }
                    """;

    static void givenBitcoinCurrentPriceRequest(Stubbing wiremock) {
        wiremock.stubFor(get("/v1/bpi/currentprice.json")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/javascript")
                        .withBody(BTC_REQUEST_RESPONSE)));
    }
}
