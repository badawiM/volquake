package com.volquake.common

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.lang.IllegalArgumentException
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.DateTimeParseException
import java.time.format.SignStyle
import java.time.temporal.ChronoField

class JackonMultiFormatDateSerializer : JsonDeserializer<Instant>(){

    companion object{

        val formats = listOf(
            DateTimeFormatter.ISO_DATE_TIME,
            DateTimeFormatter.ISO_OFFSET_DATE_TIME,
            DateTimeFormatter.ISO_INSTANT,

            //Including the below formatted to handle cases where we have long/double timestamps in db
            //(erroneously introduced in the db as jsonb hibernate-types module used its own objectMapper
            //that would write timestamps in long/double format)
            //ISO_INSTANT formatter is unable to pars such cases for some reason including a custom one
            //Would prefer a solution where we can rely on standard ISO_INSTANT
            DateTimeFormatterBuilder()
                .appendValue(ChronoField.INSTANT_SECONDS, 1, 19, SignStyle.NEVER)
                .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true )
                .toFormatter()
        )

    }

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Instant {
        var instant: Instant? = null

        //when the current token is of type double we need the big decimal representation
        //otherwise it errors on parse
        val dateText = if (p.currentToken.isNumeric && p.numberType === JsonParser.NumberType.DOUBLE){
            p.decimalValue.toString()
        } else {
            p.text
        }

        val itr = formats.iterator()
        while(instant == null && itr.hasNext()){
            try{
                instant = itr.next().parse(dateText, Instant::from)
            } catch(e: DateTimeParseException){

            }
        }
        if(instant == null ){
         throw IllegalArgumentException("Could not parse a date from value $dateText with any of the provided formats")
        }
        return instant

    }

}
