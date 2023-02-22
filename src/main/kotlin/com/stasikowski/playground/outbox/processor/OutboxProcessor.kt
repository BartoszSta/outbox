package com.stasikowski.playground.outbox.processor

import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class OutboxProcessor {

    private val log = LoggerFactory.getLogger(javaClass.name)

    @KafkaListener(
        topics = ["my.test.topic"],
            groupId = "outbox.processor",
            clientIdPrefix = "fkdsfjks",
            concurrency = "1"
    )
    fun process(message: String) {
        log.info("Received $message")
    }
}