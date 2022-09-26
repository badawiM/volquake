package com.volquake.stochastic

import org.apache.commons.math3.distribution.NormalDistribution

class NormalRandomGenerator : Function<Double> {

 val distribution = NormalDistribution(0.0,1.0)

 fun generateRandom() : Double{
  return distribution.sample()
 }

}
