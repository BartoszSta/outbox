package com.stasikowski.playground.outbox.processor

import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class OutboxProcessor2 {

    private val log = LoggerFactory.getLogger(javaClass.name)

    @KafkaListener(
        topics = ["my.another.topic"],
            groupId = "outbox.processor.my.another",
            clientIdPrefix = "outbox.processor.my.another",
            concurrency = "1"
    )
    fun process(message: String) {
        log.info("Received $message")
    }
}