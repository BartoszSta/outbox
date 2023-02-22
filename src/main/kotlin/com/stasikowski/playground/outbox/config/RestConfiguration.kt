package com.stasikowski.playground.outbox.config

import io.micrometer.core.instrument.binder.okhttp3.OkHttpObservationInterceptor
import io.micrometer.observation.ObservationRegistry
import okhttp3.OkHttpClient
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit


@Configuration
class RestConfiguration() {

    @Bean
    fun restTemplate(restTemplateBuilder: RestTemplateBuilder) = restTemplateBuilder.build()

    @Bean
    fun okHttpClient(observationRegistry: ObservationRegistry) = OkHttpClient.Builder()
        .readTimeout(1000, TimeUnit.MILLISECONDS)
        .writeTimeout(1000, TimeUnit.MILLISECONDS)
        .addInterceptor(OkHttpObservationInterceptor.builder(observationRegistry, "okhttp.requests").build())
        .build()
}