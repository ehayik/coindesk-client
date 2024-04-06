package com.github.ehayik.coindesk.application.usecase;

import an.awesome.pipelinr.Command;
import com.github.ehayik.coindesk.application.domain.PriceIndex;
import com.github.ehayik.coindesk.application.port.in.GetBtcCurrentPriceCommand;
import com.github.ehayik.coindesk.application.port.out.GetBtcCurrentPricePort;
import com.github.ehayik.coindesk.common.UseCase;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

@UseCase
@RequiredArgsConstructor
public class GetBtcCurrentPriceUseCase implements Command.Handler<GetBtcCurrentPriceCommand, Publisher<PriceIndex>> {

    private final GetBtcCurrentPricePort priceIndexPort;

    @Override
    public Publisher<PriceIndex> handle(GetBtcCurrentPriceCommand command) {
        return Mono.from(priceIndexPort.getBtcCurrentPrice()).map(PriceIndex::new);
    }
}
