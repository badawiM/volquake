package com.volquake

import RabbitMQConfig
import com.volquake.pricing.config.PricingConfiguration
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(classes = [
	RabbitMQConfig::class,
	PricingConfiguration::class
])
class VolquakeApplicationTests {

	@Test
	fun contextLoads() {
	}

}
