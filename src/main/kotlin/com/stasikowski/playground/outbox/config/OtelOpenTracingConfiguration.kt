package com.stasikowski.playground.outbox.config

import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter
import io.opentelemetry.opentracingshim.OpenTracingShim
import io.opentracing.Tracer
import org.springframework.context.annotation.Bean

//import io.opentelemetry.instrumentation.jdbc.datasource.OpenTelemetryDataSource
//import io.opentelemetry.instrumentation.kafkaclients.KafkaTelemetry
//import io.opentelemetry.opentracingshim.OpenTracingShim
//import io.opentracing.Tracer
//import io.opentracing.contrib.kafka.spring.TracingKafkaAspect
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
//import java.util.*
//import javax.sql.DataSource


@Configuration
class OtelOpenTracingConfiguration(val openTelemetry: OpenTelemetry) {

    @Bean
    fun tracer(): Tracer? = OpenTracingShim.createTracerShim(openTelemetry)


}