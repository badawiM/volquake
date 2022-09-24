package com.volquake.stochastic

import java.util.*

interface StochasticSequence<T> {

    fun getLastPriceInSequence(): Optional<T>

    fun generateFiniteSequence(sequenceLength: Int)

    fun generateFiniteSequence(sequenceLength: Int, fn: (t: T) -> Unit)

}
