package com.volquake

import EnableRabbitMq
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@EnableRabbitMq
@SpringBootApplication
class Pricing

fun main(args: Array<String>) {
	runApplication<Pricing>(*args)
}
