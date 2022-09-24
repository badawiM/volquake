package com.volquake.pricing.config

import com.volquake.common.Jackson
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket

@Configuration
class PricingConfiguration(
    @Value("\${rabbitmq.exchanges.internal}")
    private val internalExchange: String,
    @Value("\${rabbitmq.queues.prices}")
    private val notificationQueue: String,
    @Value("\${rabbitmq.routing-keys.internal-notification}")
    private val internalNotificationRoutingKey: String

    ) {

    @Bean
    fun internalTopicExchange(): TopicExchange = TopicExchange(internalExchange)

    @Bean
    fun pricesQueue() = Queue(notificationQueue)


    @Bean
    fun internalToNotificationBinding(): Binding {
        return BindingBuilder
            .bind(pricesQueue())
            .to(internalTopicExchange())
            .with(internalNotificationRoutingKey)
    }


    @Bean
    fun objectMapper() = Jackson.DEFAULT_JACKSON


    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.any())
            .build()
    }
}


