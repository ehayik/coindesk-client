package com.github.ehayik.coindesk.application.port.in;

import an.awesome.pipelinr.Command;
import com.github.ehayik.coindesk.application.domain.PriceIndex;
import org.reactivestreams.Publisher;

public class GetBtcCurrentPriceCommand implements Command<Publisher<PriceIndex>> {}
