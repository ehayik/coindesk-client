package com.github.ehayik.coindesk.application.usecase;

import an.awesome.pipelinr.Command;
import com.github.ehayik.coindesk.application.domain.PriceIndex;
import com.github.ehayik.coindesk.application.ports.in.ForRequestingBitcoinCurrentPrice;
import com.github.ehayik.coindesk.application.ports.out.ForLoadingBitcoinPrice;
import com.github.ehayik.coindesk.common.UseCase;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

@UseCase
@RequiredArgsConstructor
public class LoadBitcoinCurrentPriceUseCase
        implements Command.Handler<ForRequestingBitcoinCurrentPrice, Publisher<PriceIndex>> {

    private final ForLoadingBitcoinPrice priceIndexPort;

    @Override
    public Publisher<PriceIndex> handle(ForRequestingBitcoinCurrentPrice input) {
        return Mono.from(priceIndexPort.loadBitcoinCurrentPrice()).map(PriceIndex::new);
    }
}
