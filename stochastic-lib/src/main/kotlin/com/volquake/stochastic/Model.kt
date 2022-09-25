package com.volquake.stochastic

import com.fasterxml.jackson.annotation.JsonFormat
import java.io.Serializable
import java.math.BigDecimal
import java.time.Clock
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit

data class BidOfferPrice(val underlying: String, val bid: BigDecimal, val offer: BigDecimal, @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss") val priceDateTime: LocalDateTime) : Serializable

data class StochasticProcessParameters(val underlying: String, val mu: Double, val sigma: Double, val startPrice: BigDecimal, val frequency: Frequency = Frequency.SECOND, val startTime: LocalDateTime?)

data class StochasticProcess(val parameters : StochasticProcessParameters, val normalRandomGenerator: NormalRandomGenerator = NormalRandomGenerator(), private val zoneId : ZoneId = ZoneId.systemDefault())

open class OptionParameters(open val type: OptionType, open val r : Double = 0.0, open val expiry : Double = 0.0, open val strikePrice : Double = 0.0)

data class BlackScholesParameters(val assetPrice : Double, override val strikePrice: Double, val sigma : Double, override val r: Double, override val expiry: Double, override val type: OptionType): OptionParameters(type,r,expiry,strikePrice)

data class OptionPrices(val underlying: String, val underlyingPrice: Double, val callPrice: Double, val putPrice: Double)

fun createStochasticProcess(underlying: String, mu: Double, sigma: Double, startPrice: BigDecimal, frequency: Frequency)  =
    StochasticProcess(StochasticProcessParameters(underlying,mu,sigma,startPrice,frequency, LocalDateTime.now()))

fun createGeneralisedWienerProcess(underlying: String, mu: Double, sigma: Double, startPrice: BigDecimal, frequency: Frequency, clock: Clock)  =
    GeneralizedWienerProcess(createStochasticProcess(underlying,mu,sigma,startPrice,frequency), clock)

const val TRADING_DAYS_IN_YEAR = 252
const val MINUTES_IN_DAY = 1440
const val WEEKS_IN_YEAR = 52
const val SECONDS_IN_DAY = 28800


data class Product(val id: Int, val name: String, val description: String)


fun Frequency.toValue() : Double {
    return when(this){
        Frequency.SECOND -> 1.0.div(TRADING_DAYS_IN_YEAR).div(SECONDS_IN_DAY)
        Frequency.MINUTE -> 1.0.div(TRADING_DAYS_IN_YEAR).div(MINUTES_IN_DAY)
        Frequency.HOURLY -> 1.0.div(TRADING_DAYS_IN_YEAR).div(24)
        Frequency.DAILY -> 1.0.div(TRADING_DAYS_IN_YEAR)
        Frequency.WEEKLY -> 1.0.div(WEEKS_IN_YEAR)
    }
}

fun Frequency.toTemporalUnit() : TemporalUnit {
    return when(this){
        Frequency.SECOND -> ChronoUnit.SECONDS
        Frequency.MINUTE -> ChronoUnit.MINUTES
        Frequency.HOURLY -> ChronoUnit.HOURS
        Frequency.DAILY -> ChronoUnit.DAYS
        Frequency.WEEKLY -> ChronoUnit.WEEKS

    }
}

fun Frequency.toDuration() : Duration {
    return when(this){
        Frequency.SECOND -> Duration.ofSeconds(1)
        Frequency.MINUTE -> Duration.ofMinutes(1)
        Frequency.HOURLY -> Duration.ofMinutes(60)
        Frequency.DAILY -> Duration.ofDays(1)
        Frequency.WEEKLY -> Duration.ofDays(7)

    }
}

enum class OptionType{
    CALL,
    PUT
}


enum class Frequency{
    SECOND,
    MINUTE,
    HOURLY,
    DAILY,
    WEEKLY
}
