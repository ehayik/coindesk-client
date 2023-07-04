package com.github.ehayik.coindesk.application.usecase;

import an.awesome.pipelinr.Command;
import com.github.ehayik.coindesk.application.domain.PriceIndex;
import com.github.ehayik.coindesk.application.in.BtcCurrentPriceIndexCommand;
import com.github.ehayik.coindesk.application.out.BtcCurrentPriceIndexPort;
import com.github.ehayik.coindesk.common.UseCase;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

@UseCase
@RequiredArgsConstructor
public class GetBtcCurrentPriceIndexUseCase
        implements Command.Handler<BtcCurrentPriceIndexCommand, Publisher<PriceIndex>> {

    private final BtcCurrentPriceIndexPort priceIndexPort;

    @Override
    public Publisher<PriceIndex> handle(BtcCurrentPriceIndexCommand command) {
        return Mono.from(priceIndexPort.getBitcoinCurrentPriceIndex()).map(PriceIndex::new);
    }
}
