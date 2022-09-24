package com.volquake.pricing.factory

import com.volquake.stochastic.BidOfferPrice
import com.volquake.stochastic.GeneralizedWienerProcess
import reactor.core.publisher.Flux


interface PricersFactory : () -> Map<String, GeneralizedWienerProcess>

