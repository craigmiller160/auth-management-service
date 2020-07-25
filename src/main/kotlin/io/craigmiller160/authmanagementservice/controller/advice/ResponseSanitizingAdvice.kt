package io.craigmiller160.authmanagementservice.controller.advice

import io.craigmiller160.authmanagementservice.dto.Sanitizer
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice

@Aspect
@ControllerAdvice
class ResponseSanitizingAdvice {

    // TODO need unit tests

    @Pointcut("execution(public * io.craigmiller160.authmanagementservice.controller.*Controller.*(..))")
    fun controllerPublicMethods() { }

    @Around("controllerPublicMethods()")
    fun sanitize(joinPoint: ProceedingJoinPoint): Any? {
        val result = joinPoint.proceed(joinPoint.args)
        return result?.let { res ->
            when (res) {
                is ResponseEntity<*> -> sanitizeResponseEntity(res)
                else -> sanitizeEntity(res)
            }
        }
    }

    private fun sanitizeResponseEntity(response: ResponseEntity<*>): ResponseEntity<*> {
        val sanitizedBody = response.body?.let { sanitizeEntity(it) }
        return ResponseEntity
                .status(response.statusCodeValue)
                .headers(response.headers)
                .body(sanitizedBody)
    }

    private fun sanitizeEntity(entity: Any): Any? {
        return when (entity) {
            is Sanitizer<*> -> entity.sanitize()
            else -> entity
        }
    }

}
