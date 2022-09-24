package com.volquake

import EnableRabbitMq
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@EnableRabbitMq
@SpringBootApplication
class PriceRequest

fun main(args: Array<String>) {
	runApplication<PriceRequest>(*args)
}
