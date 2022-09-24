package com.volquake.pricerequest.controller

import com.volquake.common.logger
import com.volquake.pricerequest.configuration.StompTopics
import com.volquake.stochastic.BidOfferPrice
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.messaging.Message
import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.support.GenericMessage
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import java.time.Clock
import java.time.LocalDateTime

@RestController
class PriceRequestController(
    private val webClient: WebClient,
    private val websocketPublisher: PriceWebsocketPublisher,
    private val clock: Clock
) {

    companion object{
        const val SHORT_ID_LENGTH = 12
    }

    @GetMapping(path = ["/requestPrice/{ticker}"])
    fun requestPrice( @PathVariable("ticker") ticker: String): Flux<BidOfferPrice> {
        val subscriptionId = generateSubscriptionId()
        return webClient.get()
            .uri{ uriBuilder -> uriBuilder
                .path("/randomPriceStream/{subscriptionId}")
                .queryParam("ticker",ticker)
                .build(subscriptionId)
            }
            .retrieve()
            .bodyToFlux(BidOfferPrice::class.java)
            .doOnNext{
                websocketPublisher.publish(subscriptionId,it)
            }


    }

    private fun generateSubscriptionId() = RandomStringUtils.random(SHORT_ID_LENGTH)

}
