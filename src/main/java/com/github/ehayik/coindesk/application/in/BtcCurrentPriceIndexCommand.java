package com.github.ehayik.coindesk.application.in;

import an.awesome.pipelinr.Command;
import com.github.ehayik.coindesk.application.domain.PriceIndex;
import org.reactivestreams.Publisher;

public class BtcCurrentPriceIndexCommand implements Command<Publisher<PriceIndex>> {}
