package com.volquake.pricerequest.controller

import com.volquake.common.logger
import com.volquake.pricerequest.configuration.StompTopics
import com.volquake.stochastic.BidOfferPrice
import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.support.GenericMessage
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime

@Component
class PriceWebsocketPublisher(
    private val simpMessagingTemplate: SimpMessagingTemplate,
    private val clock: Clock
) {

    fun publish(subscriptionId: String, price: BidOfferPrice) {
        logger().info("Publishing @${LocalDateTime.now(clock)} to websocket: ${price.underlying}[${price.priceDateTime}] = ${price.bid}/${price.offer}")

        simpMessagingTemplate.send(
            "${StompTopics.topicPrefix}/$subscriptionId",
            GenericMessage(price, MessageHeaders(emptyMap()))
        )
    }

}
