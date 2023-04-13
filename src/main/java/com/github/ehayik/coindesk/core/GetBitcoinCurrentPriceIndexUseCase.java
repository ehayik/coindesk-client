package com.github.ehayik.coindesk.core;

import an.awesome.pipelinr.Command;
import com.github.ehayik.coindesk.common.UseCase;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

@UseCase
@RequiredArgsConstructor
public class GetBitcoinCurrentPriceIndexUseCase
        implements Command.Handler<BitcoinCurrentPriceIndexCommand, Publisher<PriceIndex>> {

    private final BitcoinCurrentPriceIndexPort priceIndexPort;

    @Override
    public Publisher<PriceIndex> handle(BitcoinCurrentPriceIndexCommand command) {
        return Mono.from(priceIndexPort.getBitcoinCurrentPriceIndex()).map(PriceIndex::new);
    }
}
