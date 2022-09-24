package com.volquake.stochastic

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit

fun nextPriceDateTime(lastPriceDateTime: LocalDateTime, temporalUnit: TemporalUnit): LocalDateTime {
    val latestPriceDateTIme = lastPriceDateTime.plus(1, temporalUnit)
    return if(latestPriceDateTIme.hour >= 17){
        val tomorrow = lastPriceDateTime.plus(1, ChronoUnit.DAYS)
        LocalDateTime.of(tomorrow.year, tomorrow.month, tomorrow.dayOfMonth, 9,0,0,0)
    } else{
        latestPriceDateTIme
    }
}
