package com.jfinancial.utils.stochastic

import com.jfinancial.utils.stochastic.GeneralizedWienerProcess
import com.jfinancial.utils.stochastic.StochasticProcess
import com.jfinancial.utils.stochastic.StochasticProcessParameters
import java.time.LocalDateTime

internal class GeneralizedWienerProcessTest {

    @Test
    fun testGeneratingPriceSequence() {
        val spp = StochasticProcessParameters("udon", 0.1, 0.3, 100.0, LocalDateTime.now(), Frequency.DAILY)
        val s = GeneralizedWienerProcess(StochasticProcess(spp))
        s.produceSequence(10);
        val prices = s.getPrices()
        assertEquals(10, prices.size)
    }

    @Test
    fun testGeneratingEndlessPriceSequence() {
        val spp = StochasticProcessParameters("udon", 0.1, 0.3, 100.0, LocalDateTime.now(), Frequency.DAILY)
        val s = GeneralizedWienerProcess(StochasticProcess(spp))
        assertThrows<IllegalStateException>({"kabom"}, { s.generatePrices(0, { throw IllegalStateException("kaboom") }, 10) })
    }



}
