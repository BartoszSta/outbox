package com.stasikowski.playground.outbox.config

import io.micrometer.common.lang.Nullable
import io.micrometer.tracing.Tracer
import io.micrometer.tracing.annotation.NewSpan
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import java.lang.reflect.Method

@Aspect
class NewSpanAspect(private val tracer: Tracer) {

    @Around("execution (@io.micrometer.tracing.annotation.NewSpan * *.*(..))")
    @Nullable
    @Throws(
        Throwable::class
    )
    fun observeMethod(pjp: ProceedingJoinPoint): Any? {
        val method: Method = getMethod(pjp)
        val signature = pjp.staticPart.signature
        val newSpan = tracer.nextSpan().name(signature.declaringType.simpleName + "#" + signature.name)
        try {
            tracer.withSpan(newSpan.start()).use {
                return pjp.proceed()
            }
        } finally {
            newSpan.end()
        }
    }

    @Throws(NoSuchMethodException::class)
    private fun getMethod(pjp: ProceedingJoinPoint): Method {
        val method = (pjp.signature as MethodSignature).method
        return if (method.getAnnotation(NewSpan::class.java) == null) {
            pjp.target.javaClass.getMethod(method.name, *method.parameterTypes)
        } else method
    }
}