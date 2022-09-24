package com.volquake.common

import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.type.MapType
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.time.Instant

object Jackson {

    @JvmStatic
    val DEFAULT_JACKSON: ObjectMapper = jacksonObjectMapper()
        .registerModule(JavaTimeModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true)
        .registerModule(JacksonMultiFormatDateModule)
        .registerModule(SimpleModule().setDeserializerModifier(object : BeanDeserializerModifier() {
            override fun modifyMapDeserializer(
                config: DeserializationConfig,
                type: MapType,
                beanDesc: BeanDescription,
                deserializer: JsonDeserializer<*>
            ): JsonDeserializer<*> =
                if(false){
                    //doc custom serialization here
                    throw IllegalAccessException()
                } else {
                    deserializer
                }

        }))


}

object JacksonMultiFormatDateModule : SimpleModule("MultiFormatDateModule") {

    init {
        addDeserializer(Instant::class.java, JackonMultiFormatDateSerializer())
    }

}
