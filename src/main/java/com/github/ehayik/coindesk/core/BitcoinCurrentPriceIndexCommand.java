package com.github.ehayik.coindesk.core;

import an.awesome.pipelinr.Command;
import org.reactivestreams.Publisher;

public class BitcoinCurrentPriceIndexCommand implements Command<Publisher<PriceIndex>> {}
