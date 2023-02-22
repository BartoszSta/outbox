package com.stasikowski.playground.outbox

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@SpringBootApplication
@EnableWebMvc
class OutboxApplication

fun main(args: Array<String>) {
	runApplication<OutboxApplication>(*args)
}
