package com.stasikowski.playground.outbox.service

import com.stasikowski.playground.outbox.model.Customer
import com.stasikowski.playground.outbox.repository.CustomerRepository
import io.micrometer.tracing.Tracer
import io.micrometer.tracing.annotation.NewSpan
import io.opentracing.Span
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class OutboxService(
    private val customerRepository: CustomerRepository,
    private val tracer: Tracer,
    private val openTracingTracer: io.opentracing.Tracer,
    private val restTemplate: RestTemplate,
    private val okHttpClient: OkHttpClient
) {

    //    @Observed(name = "test.call", contextualName = "test#call",
//        lowCardinalityKeyValues = [ "abc", "123", "test", "42" ])
    @NewSpan
    fun saveCustomer(firstName: String, lastName: String) {

        customerRepository.save(Customer(firstName, lastName))
//            }
//        } finally {
//            newSpan.end()
//        }

    }

    fun someFun() {
        val span: Span = openTracingTracer.buildSpan("someFun").start()
        try {
            openTracingTracer.scopeManager().activate(span).use { ignored ->
                Thread.sleep(200)
            }
        } finally {
            span.finish()
        }
    }

    fun callAnotherService(): String {
        val res =
            restTemplate.getForObject("http://localhost:8075/v1/example", ExampleResponse::class.java)
        return res?.result ?: "null"
    }

    fun callAnotherServiceOkHttp(): String {

        val request: Request = Request.Builder()
            .url("http://localhost:8075/v1/example")
            .get()
            .build()

        val response = okHttpClient.newCall(request).execute()
        return response.body.toString()
    }

    data class ExampleResponse(val result: String)
}