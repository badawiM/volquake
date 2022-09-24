package com.volquake.pricerequest.controller

import com.volquake.stochastic.BidOfferPrice
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux

@RestController
class PriceRequestController(private val webClient: WebClient) {

    @GetMapping(path = ["/requestPrice/{ticker}"])
    fun requestPrice( @PathVariable("ticker") ticker: String): Flux<BidOfferPrice> {
        val subscriptionId = "foo"
        return webClient.get()
            .uri{ uriBuilder -> uriBuilder
                .path("/randomPriceStream/{subscriptionId}")
                .queryParam("ticker",ticker)
                .build(subscriptionId, ticker)
            }
            .retrieve()
            .bodyToFlux(BidOfferPrice::class.java)

    }

}
