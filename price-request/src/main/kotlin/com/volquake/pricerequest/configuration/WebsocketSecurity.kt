//package com.volquake.pricerequest.configuration
//
//import org.springframework.context.annotation.Configuration
//import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry
//import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer
//
//@Configuration
//class WebsocketSecurity : AbstractSecurityWebSocketMessageBrokerConfigurer() {
//
//
//    override fun configureInbound(messages: MessageSecurityMetadataSourceRegistry) {
//        messages.anyMessage().permitAll()
//    }
//
//    override fun sameOriginDisabled(): Boolean {
//        return true
//    }
//
//    //@Bean
//    //fun csrfTokenRepository(): CookieCsrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse()
//}
