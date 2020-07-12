package io.craigmiller160.authmanagementservice.controller.advice

import io.craigmiller160.authmanagementservice.dto.ClientList
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import java.lang.RuntimeException

@Aspect
@ControllerAdvice
class ResponseSanitizingAdvice {

    @Pointcut("execution(public * io.craigmiller160.authmanagementservice.controller.*Controller.*(..))")
    fun controllerPublicMethods() { }

//    @AfterReturning("controllerPublicMethods()", returning = "result")
//    fun sanitize(joinPoint: JoinPoint, result: Any?) {
//        result?.let { res ->
//            when (res) {
//                is ResponseEntity<*> -> {
//                    val body = res.body
//                    when(body) {
//                        is ClientList -> {
//                            val newClients = body.clients.map { client -> client.copy(clientSecret = "") }
//                            println(newClients) // TODO delete this
//                        }
//                        else -> throw RuntimeException("No match")
//                    }
//                }
//                else -> throw RuntimeException("No match") // TODO delete this
//            }
//        }
//    }

    @Around("controllerPublicMethods()")
    fun sanitize(joinPoint: ProceedingJoinPoint): Any? {
        val result = joinPoint.proceed(joinPoint.args)
        return result?.let { res ->
            when (res) {
                is ResponseEntity<*> -> {
                    println("IsResponseEntity")
                    ResponseEntity.ok("Hello")
                }
                else -> result
            }
        }
    }

}
