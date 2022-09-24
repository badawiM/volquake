import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class RabbitMQConfig() {


    @Bean
    fun connectionFactory(): CachingConnectionFactory{
        return CachingConnectionFactory("localhost")
    }

    @Bean
    fun amqpAdmin(): RabbitAdmin {
        return RabbitAdmin(connectionFactory())
    }

    @Bean
    fun amqpTemplate(objectMapper: ObjectMapper, connectionFactory: ConnectionFactory): AmqpTemplate {
        val rabbitTemplate = RabbitTemplate(connectionFactory)
        rabbitTemplate.messageConverter = jacksonConverter(objectMapper)
        return rabbitTemplate
    }

    @Bean
    fun simpleRabbitListenerContainerFactory(messageConverter: MessageConverter, connectionFactory: ConnectionFactory): SimpleRabbitListenerContainerFactory {
        val factory = SimpleRabbitListenerContainerFactory()
        factory.setConnectionFactory(connectionFactory)
        factory.setMessageConverter(messageConverter)
        return factory;
    }

    @Bean
    fun jacksonConverter(objectMapper: ObjectMapper) :MessageConverter = Jackson2JsonMessageConverter(objectMapper)

}
