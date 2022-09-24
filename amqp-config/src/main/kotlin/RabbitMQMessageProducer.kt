

import com.volquake.common.logger
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.stereotype.Component;

@Component
class RabbitMQMessageProducer(private val amqpTemplate: AmqpTemplate, private val rabbitAdmin: RabbitAdmin) {

    fun publish( payload: Any,  exchange: String, routingKey: String) {
        logger().info("Publishing to {} using routingKey {}. Payload: {}", exchange, routingKey, payload)
        amqpTemplate.convertAndSend(exchange, routingKey, payload)
        logger().trace("Published to {} using routingKey {}. Payload: {}", exchange, routingKey, payload)
    }

}
