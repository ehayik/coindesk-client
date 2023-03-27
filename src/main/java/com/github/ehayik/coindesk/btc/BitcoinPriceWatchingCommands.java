package com.github.ehayik.coindesk.btc;

import static java.time.Duration.ZERO;
import static org.springframework.shell.standard.ShellOption.NULL;

import com.github.ehayik.coindesk.shell.ShellHelper;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.annotation.PreDestroy;
import javax.money.MonetaryAmount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Slf4j
@ShellComponent
@SuppressWarnings("unused")
class BitcoinPriceWatchingCommands {

    private static final String REFRESH_RATE_HELP =
            """
            The price index refresh rate in:
                - ns for nanoseconds
                - us for microseconds
                - ms for milliseconds
                - s for seconds
                - m for minutes
                - h for hours
                - d for days
            [Optional, default = <none>]
            """;

    private final ShellHelper shellHelper;
    private final Sinks.Many<Boolean> stopEmitter;
    private final BitcoinPriceIndexService priceIndexService;

    BitcoinPriceWatchingCommands(ShellHelper shellHelper, BitcoinPriceIndexService priceIndexService) {
        this.shellHelper = shellHelper;
        this.priceIndexService = priceIndexService;
        stopEmitter = Sinks.many().multicast().onBackpressureBuffer();
    }

    @PreDestroy
    @ShellMethod(
            value = "Stop Bitcoin Price Index (BPI) requests.",
            key = {"stop bitcoin", "stop btc"})
    void stopBitcoinPriceRequest() {
        stopEmitter.tryEmitNext(true);
    }

    @ShellMethod(
            value = "Watch the Bitcoin Price Index (BPI) in real-time.",
            key = {"bitcoin", "btc"})
    void watchBitcoinPrice(
            @ShellOption(value = "-rf", defaultValue = NULL, help = REFRESH_RATE_HELP) Duration refreshRate) {

        if (refreshRate == null || refreshRate == ZERO) {
            priceIndexService //
                    .getPriceIndexOnRealTime()
                    .blockOptional()
                    .ifPresent(this::printBitcoinPriceTable);
            return;
        }

        Flux.interval(ZERO, refreshRate)
                .flatMap(x -> priceIndexService.getPriceIndexOnRealTime())
                .takeUntilOther(stopEmitter.asFlux())
                .subscribe(this::printBitcoinPriceTable);
    }

    private void printBitcoinPriceTable(List<MonetaryAmount> prices) {
        var currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        shellHelper.printInfo("Bitcoin Price Index (%s)".formatted(currentTime));
        var tableBuilder = new TableBuilder(asArrayTableModel(prices));
        tableBuilder.addFullBorder(BorderStyle.oldschool);
        shellHelper.print(tableBuilder.build().render(80));
    }

    private static ArrayTableModel asArrayTableModel(List<MonetaryAmount> prices) {
        var formattedPrices = prices //
                .stream()
                .map(MonetaryAmount::toString)
                .toArray(String[]::new);

        return new ArrayTableModel(new Object[][] {formattedPrices});
    }
}
