package com.volquake.pricerequest.controller

import com.volquake.common.logger
import com.volquake.pricerequest.configuration.stompTopic
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Controller
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent
import org.springframework.web.socket.messaging.SessionSubscribeEvent
import reactor.core.Disposable
import java.time.Clock

@Controller
class PriceStompController(
    private val priceRequestController: PriceRequestController,
    private val websocketPublisher: PriceWebsocketPublisher,
    private val clock: Clock
) {

    private val subscribedStreams = mutableMapOf<SessionSubscriptionId, SubscribedStream>()

    @EventListener
    fun handleSubscription(event: SessionSubscribeEvent) {
        val sessionId = event.header("simpSessionId")
        val subscriptionId = event.header("simpSubscriptionId")
        val destination = event.header("simpDestination")
        if (!destination.startsWith("/topic/pricestream")) {
            error("Wrong destination")
        }
        val streamId = destination.split("/").last()
        logger().info("About to call getPriceStream($streamId)")
        val topic = stompTopic("pricestream/$streamId")
        val stream = priceRequestController.requestPrice("VOD.L")
            .doOnError {
                logger().error("Error occured getting price", it)
            }
            .doOnCancel() {
                logger().error("Price stream cancelled",)
            }
            .subscribe {
                websocketPublisher.publish(topic, it)
            }
        logger().info("Subscribed to stream with topic = ${topic}")
        subscribedStreams[sessionSubscription(sessionId,subscriptionId)] = SubscribedStream(streamId, stream)
    }

    @EventListener
    fun handleSessionDisconnect(event: SessionDisconnectEvent){
        val subscriptionsToDispose = this.subscribedStreams.keys.filter { it.startsWith(event.sessionId) }
        logger().info("Websocket session ${event.sessionId} disconnected, will terminate ${subscriptionsToDispose.size} subscriptions")
        subscriptionsToDispose.forEach{ this.unsubscribe(it)}
    }

    private fun unsubscribe(sessionSubscription: String){
        val subscribedStream = subscribedStreams[sessionSubscription] ?: return
        priceRequestController.cancelPriceRequest(subscribedStream)
        subscribedStream.subcription.dispose()
        logger().info("Unsubscribed from subscription $sessionSubscription")
    }

    private fun sessionSubscription(simpSubscriptionId: String, simpSessionId: String): SessionSubscriptionId = "$simpSessionId/$simpSubscriptionId"

    private fun AbstractSubProtocolEvent.header(name: String): String{
        return this.message.headers[name]?.toString() ?: error("Cannot access h" +
                "eader $name as it was not present on the message")

    }

}

typealias SessionSubscriptionId = String

data class SubscribedStream(val streamId: String, val subcription: Disposable)
