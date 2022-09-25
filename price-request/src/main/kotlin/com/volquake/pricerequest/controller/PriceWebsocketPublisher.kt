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
        val destination = "${StompTopics.topicPrefix}/pricestream"
        logger().info("Publishing @${LocalDateTime.now(clock)} to websocket destination $destination: ${price.underlying}[${price.priceDateTime}] = ${price.bid}/${price.offer}")
        simpMessagingTemplate.convertAndSend(
            destination,
            price,
        )
    }

}
