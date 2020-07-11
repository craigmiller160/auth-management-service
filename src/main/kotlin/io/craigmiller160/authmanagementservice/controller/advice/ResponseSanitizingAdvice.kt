package io.craigmiller160.authmanagementservice.controller.advice

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.web.bind.annotation.ControllerAdvice

@Aspect
@ControllerAdvice
class ResponseSanitizingAdvice {

    @Pointcut("execution(public * io.craigmiller160.authmanagementservice.controller.*Controller.*(..))")
    fun controllerPublicMethods() { }

    @AfterReturning("controllerPublicMethods()", returning = "result")
    fun sanitize(joinPoint: JoinPoint, result: Any?) {
        println("RESULT: $result") // TODO delete this
    }

}
