package com.volquake.pricing.factory

import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.volquake.pricing.publisher.PricersFactoryFunction
import com.volquake.stochastic.GeneralizedWienerProcess
import com.volquake.stochastic.StochasticProcessParameters
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import org.springframework.util.ResourceUtils
import java.time.Clock

@Component
@Primary
class PricersFactoryFromConfigFile(
    @Value("\${publisher.configurationFile}") filename: String,
    private val clock: Clock
): PricersFactory {

    private val parameters = readCsvFile<StochasticProcessParameters>(filename)

    override fun invoke(): Map<String, GeneralizedWienerProcess> = PricersFactoryFunction(clock)(parameters)

    private inline fun <reified T> readCsvFile(fileName: String): List<T> {
        val resource = ResourceUtils.getFile("classpath:$fileName");
        val csvMapper = CsvMapper().apply { registerModule(KotlinModule()) }
        resource.bufferedReader().use { reader ->
            return csvMapper
                .readerFor(T::class.java)
                .with(CsvSchema.emptySchema().withHeader())
                .readValues<T>(reader)
                .readAll()
                .toList()
        }
    }

}

class PricersFactoryFromParameters(private val parameters : List<StochasticProcessParameters>, private val clock: Clock = Clock.systemUTC()):
    PricersFactory {
    override fun invoke(): Map<String, GeneralizedWienerProcess> = PricersFactoryFunction(clock)(parameters)
}
