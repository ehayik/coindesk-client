package com.github.ehayik.coindesk.application.ports.in;

import an.awesome.pipelinr.Command;
import com.github.ehayik.coindesk.application.domain.PriceIndex;
import org.reactivestreams.Publisher;

public class ForRequestingBitcoinCurrentPrice implements Command<Publisher<PriceIndex>> {}
