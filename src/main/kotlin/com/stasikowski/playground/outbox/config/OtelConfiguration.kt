package com.stasikowski.playground.outbox.config

import io.micrometer.observation.ObservationRegistry
import io.micrometer.observation.aop.ObservedAspect
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OtelConfiguration {

    @Bean
    fun spanExporter() = OtlpHttpSpanExporter.getDefault()

    @Bean
    fun observedAspect(observationRegistry: ObservationRegistry): ObservedAspect {
        return ObservedAspect(observationRegistry)
    }

    @Bean
    fun newSpanAspect(tracer: io.micrometer.tracing.Tracer) = NewSpanAspect(tracer)
}