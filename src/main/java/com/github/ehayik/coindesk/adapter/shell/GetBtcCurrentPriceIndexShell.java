package com.github.ehayik.coindesk.adapter.shell;

import static java.time.Duration.ZERO;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.springframework.shell.standard.ShellOption.NULL;

import an.awesome.pipelinr.Pipeline;
import com.github.ehayik.coindesk.application.domain.PriceIndex;
import com.github.ehayik.coindesk.application.in.BtcCurrentPriceIndexCommand;
import java.time.Duration;
import java.time.LocalDateTime;
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
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Slf4j
@ShellComponent
@SuppressWarnings("unused")
public class GetBtcCurrentPriceIndexShell {

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
            """;

    private final ShellHelper shellHelper;
    private final Sinks.Many<Boolean> stopEmitter;
    private final Pipeline pipeline;

    GetBtcCurrentPriceIndexShell(ShellHelper shellHelper, Pipeline pipeline) {
        this.shellHelper = shellHelper;
        this.pipeline = pipeline;
        stopEmitter = Sinks.many().multicast().onBackpressureBuffer();
    }

    @PreDestroy
    @ShellMethod(
            value = "Stop displaying the Bitcoin Price Index (BPI).",
            key = {"stop bitcoin", "stop btc"})
    public void stopBitcoinPriceRequest() {
        stopEmitter.tryEmitNext(true);
    }

    @ShellMethod(
            value = "Display the Bitcoin Price Index (BPI) in real-time.",
            key = {"bitcoin", "btc"})
    public void watchBitcoinPrice(
            @ShellOption(
                            value = {"--watch", "-w"},
                            defaultValue = NULL,
                            help = REFRESH_RATE_HELP)
                    Duration refreshRate) {

        var bitcoinPriceIndex = Mono.from(new BtcCurrentPriceIndexCommand().execute(pipeline));

        if (refreshRate == null || refreshRate == ZERO) {
            bitcoinPriceIndex.blockOptional().ifPresent(this::printBitcoinPriceTable);
            return;
        }

        Flux.interval(ZERO, refreshRate)
                .flatMap(x -> bitcoinPriceIndex)
                .takeUntilOther(stopEmitter.asFlux())
                .subscribe(this::printBitcoinPriceTable);
    }

    private void printBitcoinPriceTable(PriceIndex priceIndex) {
        var currentTime = LocalDateTime.now().format(ofPattern("dd/MM/yyyy HH:mm:ss"));
        shellHelper.printInfo("Bitcoin Price Index (%s)".formatted(currentTime));

        var tableBuilder = new TableBuilder(asArrayTableModel(priceIndex));
        tableBuilder.addFullBorder(BorderStyle.oldschool);
        shellHelper.print(tableBuilder.build().render(80));
    }

    private static ArrayTableModel asArrayTableModel(PriceIndex priceIndex) {
        var formattedPrices = priceIndex //
                .getPrices()
                .map(MonetaryAmount::toString)
                .toArray(String[]::new);

        return new ArrayTableModel(new Object[][] {formattedPrices});
    }
}
