package com.volquake.pricing

import com.volquake.stochastic.BidOfferPrice
import reactor.core.publisher.Flux


interface PriceStreams {

    fun pricesStream(subscriptionId: String): PriceStream

    fun priceStreamForTicker(subscriptionId: String, ticker: String): PriceStream

}

typealias PriceStream = Flux<BidOfferPrice>
