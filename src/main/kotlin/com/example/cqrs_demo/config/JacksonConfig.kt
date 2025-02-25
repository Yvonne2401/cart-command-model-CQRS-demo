package com.example.cqrs_demo.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import community.flock.wirespec.integration.jackson.kotlin.WirespecModuleKotlin
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.web.reactive.function.client.WebClient


@Configuration
class JacksonConfig {

    @Bean
    @Primary
    fun objectMapper(): ObjectMapper {
        val wirespecModuleKotlin = WirespecModuleKotlin()
        return ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .registerModules(wirespecModuleKotlin)
            .findAndRegisterModules()
    }

    @Bean(name = [ "wirespecSpringWebClient"])
    fun webClient(): WebClient {
        return WebClient.builder().baseUrl("http://localhost:8080").build()
    }

}