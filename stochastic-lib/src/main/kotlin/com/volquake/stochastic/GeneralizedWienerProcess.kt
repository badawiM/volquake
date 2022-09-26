package com.volquake.stochastic

import com.volquake.common.*
import java.lang.NumberFormatException
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.Clock
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.sqrt

class GeneralizedWienerProcess(
    val process: StochasticProcess,
    val clock: Clock
) {

    val parameters = process.parameters
    val name = parameters.underlying

    private val dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    private val decimalFormat = DecimalFormat("###,###.###")
    private val bidAsk = createInitialBidOffer()

    private fun createInitialBidOffer(): AtomicReference<BidOfferPrice> =
        AtomicReference(
            BidOfferPrice(
                process.parameters.underlying,
                process.parameters.startPrice,
                process.parameters.startPrice,
                process.parameters.startTime ?: LocalDateTime.now(clock)
            )
        )


    fun generatePrice(): BidOfferPrice {
        try {
            val lastPrice = this.bidAsk.get()
            val lastOfferPrice = lastPrice.offer
            var ask = calculateNewPrice(lastOfferPrice).setScale(2,RoundingMode.HALF_UP)
            if (ask.isNegative()) {
                ask = BigDecimal.ZERO
            }
            val change = calculateChangeAsPercentage(lastOfferPrice, ask, 4)
            logger().info("Generated new price ${format(ask)} for $name -> ${format(change)}% at ${dtFormatter.format(LocalDateTime.now())}")
            val nxDateTime = nextPriceDateTime(lastPrice.priceDateTime, parameters.frequency.toTemporalUnit())
            val newPrice =  BidOfferPrice(name, calculateBidPrice(ask), ask, nxDateTime)
            bidAsk.set(newPrice)
            return newPrice

        } catch (e: Exception) {
            logger().error("Failed generating price", e)
            throw e
        }
    }

    private fun calculateNewPrice(price: BigDecimal) = price.plus(calculateDrift(price)).plus(calculateNoise(price))

    private fun calculateBidPrice(ask: BigDecimal): BigDecimal {
        return if(ask == BigDecimal.ZERO){
            BigDecimal.ZERO
        } else {
            val spread = scaledZ(ask).abs().divide(100.0.toBigDecimal())
            val bid = ask.minus(spread) //substracts 1% of the noise
            check(bid.isPositive()){ "Bid cannot be negative: ask is $ask" }
            return bid.round(MathContext.DECIMAL64).setScale(2, RoundingMode.HALF_UP)
        }
    }

    private fun calculateDrift(price: BigDecimal) =
        price.times(process.parameters.mu.asBigDecimal()).times(process.parameters.frequency.toValue().asBigDecimal())

    private fun calculateNoise(price: BigDecimal): BigDecimal = scaledZ(price).times(process.parameters.sigma.asBigDecimal())
            .times(sqrt(process.parameters.frequency.toValue()).asBigDecimal())

    private fun scaledZ(price: BigDecimal) = z().times(price)

    private fun z(): BigDecimal {
        val z = process.normalRandomGenerator.generateRandom()
        return if(z.isNaN()){
            z() //no idea why the normal generator sometimes returns NaN - maybe because its a bit shit...
        } else{
            z.toBigDecimal()
        }
    }

    private fun format(v: Any) = decimalFormat.format(v)


    companion object {
        fun createGWP(underlying: String, mu: Double, sigma: Double, startPrice: BigDecimal, clock: Clock, startTime: LocalDateTime = LocalDateTime.now(clock), frequency: Frequency = Frequency.SECOND): GeneralizedWienerProcess {
            val process = StochasticProcess(StochasticProcessParameters(underlying, mu, sigma, startPrice, frequency, startTime), NormalRandomGenerator())
            return GeneralizedWienerProcess(process, clock)
        }
    }


}
