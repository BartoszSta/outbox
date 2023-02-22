package com.stasikowski.playground.outbox.config

//import io.opentelemetry.instrumentation.kafkaclients.TracingConsumerInterceptor
//import io.opentelemetry.instrumentation.kafkaclients.TracingProducerInterceptor

//import io.opentracing.contrib.kafka.spring.TracingConsumerFactory
//import io.opentracing.contrib.kafka.spring.TracingProducerFactory

import com.stasikowski.playground.outbox.kafka.OutboxProducerInterceptor
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import io.micrometer.observation.ObservationRegistry
import io.micrometer.observation.aop.ObservedAspect
import io.micrometer.tracing.Tracer
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.web.filter.ServerHttpObservationFilter


@Configuration
private class OutboxKafkaConfig(
//    val otelConfiguration: OtelConfiguration, val kafkaTelemetry: KafkaTelemetry
) {

    private val log = LoggerFactory.getLogger(OutboxKafkaConfig::class.java)

//    @Bean
//    fun restTemplate() = builder.build()


    @Bean
    fun kafkaTemplate(producerFactory: ProducerFactory<Any, Any>): KafkaTemplate<Any, Any> {
        val kafkaTemplate = KafkaTemplate(producerFactory)
        kafkaTemplate.setObservationEnabled(true)
        return kafkaTemplate
    }

    @Bean
    fun producerFactory(kafkaProducerConfig: Map<String, Any?>): ProducerFactory<Any, Any> =
        DefaultKafkaProducerFactory(kafkaProducerConfig)

    @Bean
    fun kafkaProducerConfig() = mapOf(
        ProducerConfig.ACKS_CONFIG to "all",
        ProducerConfig.LINGER_MS_CONFIG to "1",
        ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG to "true",
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.qualifiedName,
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.qualifiedName,
        ProducerConfig.RETRIES_CONFIG to Int.MAX_VALUE,
        ProducerConfig.INTERCEPTOR_CLASSES_CONFIG to OutboxProducerInterceptor::class.qualifiedName,
        CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG to "localhost:29092",
        ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG to true,
    )

    @Bean
    fun kafkaListenerContainerFactory(consumerFactory: ConsumerFactory<Any, Any>): KafkaListenerContainerFactory<*> {
        val factory = ConcurrentKafkaListenerContainerFactory<Any, Any>()
        factory.consumerFactory = consumerFactory
        factory.containerProperties.ackMode = ContainerProperties.AckMode.COUNT_TIME
        factory.containerProperties.ackTime = 5000
        factory.containerProperties.ackCount = 10
        factory.containerProperties.isObservationEnabled = true
        return factory
    }

    @Bean
    fun consumerFactory(consumerProperties: Map<String, Any?>, meterRegistry: MeterRegistry): ConsumerFactory<Any, Any> {

        val defaultKafkaConsumerFactory = DefaultKafkaConsumerFactory<Any, Any>(consumerProperties)
        defaultKafkaConsumerFactory.addListener(MicrometerConsumerListener(meterRegistry, listOf(Tag.of("sss", "ddd"))))
        return defaultKafkaConsumerFactory
}

    @Bean
    fun kafkaAdmin(): KafkaAdmin? {
        val configs: MutableMap<String, Any> = HashMap()
        configs[AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:29092"
        return KafkaAdmin(configs)
    }

    @Bean
    fun consumerProperties(): Map<String, Any?> = mapOf(
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.qualifiedName,
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.qualifiedName,
        CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG to "localhost:29092",
        ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG to true,
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
    )



}
