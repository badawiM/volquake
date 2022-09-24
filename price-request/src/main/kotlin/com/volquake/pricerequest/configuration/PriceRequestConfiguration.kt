package com.volquake.pricerequest.configuration

import io.netty.handler.logging.LogLevel
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import reactor.netty.transport.logging.AdvancedByteBufFormat
//import springfox.documentation.builders.PathSelectors
//import springfox.documentation.builders.RequestHandlerSelectors
//import springfox.documentation.spi.DocumentationType
//import springfox.documentation.spring.web.plugins.Docket
import java.time.Clock

@Configuration
class PriceRequestConfiguration(
    @Value("\${priceRequest.httpClient.wiretap:false}")
    private val wiretap: Boolean
 ) {

//    @Bean
//    fun api(): Docket {
//        return Docket(DocumentationType.SWAGGER_2)
//            .select()
//            .apis(RequestHandlerSelectors.any())
//            .paths(PathSelectors.any())
//            .build()
//    }


    @Bean
    fun clock() : Clock = Clock.systemDefaultZone()

    @Bean
    fun httpClient(): HttpClient {
        return if (wiretap) {
            HttpClient.create().wiretap(
                "reactor.netty.http.client.HttpClient",
                LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)
        } else {
            HttpClient.create()
        }
    }

    @Bean
    fun webClient(httpClient: HttpClient): WebClient {
        return WebClient
            .builder()
            .baseUrl("http://localhost:8080")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_EVENT_STREAM_VALUE)
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .build()
    }
}

