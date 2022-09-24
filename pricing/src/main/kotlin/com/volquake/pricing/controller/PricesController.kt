package com.volquake.pricing.controller

import com.volquake.stochastic.BidOfferPrice
import com.volquake.pricing.PriceStreams
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import java.time.Duration
import java.time.LocalTime


@RestController
@ControllerAdvice
class PricesController(private val pricePublisher: PriceStreams) {

    @GetMapping(path = ["/randomPricesStream/{subcriptionId}"], produces = ["text/event-stream"])
    fun randomPricesStream(@PathVariable("subcriptionId") subcriptionId: String): Flux<BidOfferPrice> = pricePublisher.pricesStream(subcriptionId)

    @GetMapping(path = ["/randomPriceStream/{subcriptionId}"], produces = ["text/event-stream"])
    fun randomPriceStream(@PathVariable("subcriptionId") subcriptionId: String, @RequestParam("ticker") ticker: String): Flux<BidOfferPrice> =
        pricePublisher.priceStreamForTicker(subcriptionId, ticker)

    @GetMapping(path = ["/test"], produces = ["text/event-stream"])
    fun test(): Flux<String> =  Flux.interval(Duration.ofSeconds(1))
        .map{ LocalTime.now().toString()}

    @GetMapping(path = ["/tests"], produces = ["text/event-stream"])
    fun tests(): Flux<Int> {
        val iterable = generateSequence<Int>(1) { it + 1 }.take(100).asIterable()
        return Flux.fromIterable(iterable)
    }

}
