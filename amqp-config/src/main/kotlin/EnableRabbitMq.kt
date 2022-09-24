import org.springframework.context.annotation.Import

@Import(RabbitMQConfig::class, RabbitMQMessageProducer::class)
annotation class EnableRabbitMq()
