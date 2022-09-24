package com.volquake.pricing.publisher

import RabbitMQMessageProducer
import com.volquake.stochastic.BidOfferPrice
import org.springframework.stereotype.Component

interface PricePublisher {

    fun publishPrice(price: BidOfferPrice, subscriptionId: String)

}

@Component
class PricePublisherImpl(private val msgProducer:  RabbitMQMessageProducer): PricePublisher {
    override fun publishPrice(price: BidOfferPrice, subscriptionId: String) {
        msgProducer.publish(price, "internal.exchange",
            "internal.prices.${subscriptionId}")
    }

}
