package com.volquake.pricerequest.configuration

import com.volquake.common.Jackson
import com.volquake.common.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.converter.DefaultContentTypeResolver
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.converter.MessageConverter
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.util.MimeTypeUtils
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration
import org.springframework.web.socket.server.standard.UndertowRequestUpgradeStrategy
import org.springframework.web.socket.server.support.DefaultHandshakeHandler


@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig(
    @Value("\${websockets.sockJs.enabled:false}") val sockJsEnabled: Boolean

) : WebSocketMessageBrokerConfigurer {

    init {
        logger().info("WebSocketMessageBrokerConfigurer is using sockJs = $sockJsEnabled")
    }

    companion object{
        const val WEBSOCKET_LIMIT_IN_KB = 192
    }

    override fun configureWebSocketTransport(registration: WebSocketTransportRegistration) {
        registration.setMessageSizeLimit(WEBSOCKET_LIMIT_IN_KB * 1024)
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker(StompTopics.topicPrefix).setTaskScheduler(heartBeatScheduler())
        registry.setUserDestinationPrefix("/user")
        registry.setApplicationDestinationPrefixes("/commands")
        registry.setPreservePublishOrder(true)
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        //allow from any origin as we're connected to the Gateway

        val stompRegistration = registry.addEndpoint("/stomp")
           .setAllowedOriginPatterns("**")
            .setHandshakeHandler(DefaultHandshakeHandler(UndertowRequestUpgradeStrategy()))
        if(sockJsEnabled){
            stompRegistration.withSockJS()
                //.setClientLibraryUrl( "https://cdn.jsdelivr.net/npm/sockjs-client@1.3.0/dist/sockjs.min.js" );
        }
    }



    override fun configureMessageConverters(messageConverters: MutableList<MessageConverter>): Boolean {
        val resolver = DefaultContentTypeResolver()
        resolver.defaultMimeType = MimeTypeUtils.APPLICATION_JSON
        val converter = MappingJackson2MessageConverter()
        converter.objectMapper = Jackson.DEFAULT_JACKSON
        converter.contentTypeResolver = resolver
        messageConverters.add(converter)
        return false
    }

    @Bean
    fun heartBeatScheduler() = ThreadPoolTaskScheduler()

}
