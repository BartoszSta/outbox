package com.stasikowski.playground.outbox.controler

import com.stasikowski.playground.outbox.exception.SomeException
import com.stasikowski.playground.outbox.service.OutboxService
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("v1")
class OutboxControler(val kafkaTemplate: KafkaTemplate<Any, Any>, val outboxService: OutboxService) {

    private val log = LoggerFactory.getLogger(javaClass::class.java)

    @GetMapping("/outbox", produces = ["application/json"])
    fun helloWorld(): String {
        kafkaTemplate.send("my.test.topic", "value")
        outboxService.saveCustomer("fdsfds", "gfdgfdgd")
        outboxService.someFun()
        val res = outboxService.callAnotherService()
        log.info("Returned result $res")
        outboxService.callAnotherServiceOkHttp()
        return """ {"hello": "world"}    """
    }

    @GetMapping("/outboxError", produces = ["application/json"])
    fun helloWorld2(): String {
       if (true) {
           throw SomeException("aaaa")
       }
        return """ {"hello": "world"}    """
    }
}