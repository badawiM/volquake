package com.volquake.pricing.publisher

import com.volquake.pricing.PriceStreams
import com.volquake.pricing.PriceStream
import com.volquake.stochastic.createGeneralisedWienerProcess
import com.volquake.stochastic.GeneralizedWienerProcess
import com.volquake.stochastic.StochasticProcessParameters
import com.volquake.pricing.factory.PricersFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.time.Clock
import java.time.Duration
import java.util.*

@Component
class RandomPricePublisher(
    @Value("\${publisher.intervalInMills}")
    private val intervalInMills: Int,
    private val pricersFactory: PricersFactory,
    private val pricePublisher: PricePublisher,
    @Value("\${publisher.publishAsync:false}")
    private val publishAsync: Boolean
) : AbstractPricePublisher(intervalInMills, pricersFactory){

    private val random = Random()

    override fun pricesStream(subscriptionId: String): PriceStream = createBidOfferPublisher(subscriptionId)


    override fun priceStreamForTicker(subscriptionId: String, ticker: String): PriceStream = createPriceStream(interval, subscriptionId, ticker)

    private fun createBidOfferPublisher(subscriptionId: String) = createPriceStream(interval, subscriptionId)

    private inline fun getRandomPrices() = getPricer(randomKey()).generatePrice()

    private inline fun getRandomPrice(ticker: String) = getPricer(ticker).generatePrice()

    private inline fun randomKey() =  keys[random.nextInt(keys.size)]

    private fun createPriceStream(interval: Duration, subscriptionId: String, ticker: String? = null): PriceStream {

        return Flux.interval(interval)
            .map { if(ticker == null){ getRandomPrices() } else { getRandomPrice(ticker) } }
            .doOnNext {
                if (this.publishAsync) {
                    pricePublisher.publishPrice(it, subscriptionId)
                }
            }
            .share()
    }
}



abstract class AbstractPricePublisher(intervalInMills: Int, pricersFactory: PricersFactory) : PriceStreams {
    private val pricers: Map<String, GeneralizedWienerProcess> = pricersFactory()
    protected val keys = pricers.keys.toList()
    val interval = Duration.ofMillis(intervalInMills.toLong())

    protected fun getPricer(key: String) = pricers[key] ?: throw IllegalArgumentException("Pricer not found for $")
}



class PricersFactoryFunction(private val clock: Clock) : (List<StochasticProcessParameters>) -> Map<String, GeneralizedWienerProcess>{
    override fun invoke(parameters: List<StochasticProcessParameters>): Map<String, GeneralizedWienerProcess> =
        parameters.map { createGeneralisedWienerProcess(it.underlying, it.mu, it.sigma, it.startPrice, it.frequency, clock ) }
            .associateBy { it.name }

}
