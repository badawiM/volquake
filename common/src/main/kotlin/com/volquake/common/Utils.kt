package com.volquake.common

import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.ln

fun <T : Any> T.logger() = LoggerFactory.getLogger(javaClass)

fun Double.asBigDecimal() = BigDecimal(this, MathContext.DECIMAL64)

fun BigDecimal.isPositive() = this.compareTo(BigDecimal.ZERO) > 0

fun BigDecimal.isNegative() = this.compareTo(BigDecimal.ZERO) < 0

fun BigDecimal.isZero() = this.compareTo(BigDecimal.ZERO) == 0

fun calculateChangeAsPercentage(price: BigDecimal, ask: BigDecimal, scale: Int, roundingMode: RoundingMode = RoundingMode.HALF_UP): BigDecimal {
    val change =  ln(price.divide(ask, scale, roundingMode).toDouble()) * 100
    return BigDecimal.valueOf(change).round(MathContext.DECIMAL64).setScale(scale, roundingMode)
}


