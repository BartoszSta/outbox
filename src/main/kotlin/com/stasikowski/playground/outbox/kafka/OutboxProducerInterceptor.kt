package com.stasikowski.playground.outbox.kafka

import org.apache.kafka.clients.producer.ProducerInterceptor
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import org.slf4j.LoggerFactory
import java.lang.Exception

//java -javaagent:..\..\opentelemetry-javaagent.jar -jar .\outbox-0.0.1-SNAPSHOT.jar -Dotel.traces.exporter=jaeger -Dotel.exporter.jaeger.endpoint=http://localhost:14268

// java "-Dotel.traces.exporter=jaeger" "-Dotel.exporter.jaeger.endpoint=http://localhost:14250" -javaagent:..\..\opentelemetry-javaagent.jar -jar .\outbox-0.0.1-SNAPSHOT.jar

// java "-Dotel.traces.exporter=jaeger" "-Dotel.exporter.jaeger.endpoint=http://localhost:14250" -jar .\outbox-0.0.1-SNAPSHOT.jar

// java -javaagent:..\..\opentelemetry-javaagent.jar -jar .\outbox-0.0.1-SNAPSHOT.jar

class OutboxProducerInterceptor: ProducerInterceptor<Any, Any> {

    private val log = LoggerFactory.getLogger(javaClass.name)

    override fun configure(configs: MutableMap<String, *>?) {
    }

    override fun close() {
    }

    override fun onAcknowledgement(metadata: RecordMetadata?, exception: Exception?) {
    }

    override fun onSend(record: ProducerRecord<Any, Any>): ProducerRecord<Any, Any> {
        log.info("Produced record to topic ${record.topic()}")
        return record
    }


}