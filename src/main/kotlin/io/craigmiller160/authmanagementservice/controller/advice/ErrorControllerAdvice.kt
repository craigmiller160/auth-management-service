package io.craigmiller160.authmanagementservice.controller.advice

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.servlet.http.HttpServletRequest
import io.craigmiller160.authmanagementservice.dto.Error
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.ExceptionHandler

@RestControllerAdvice
class ErrorControllerAdvice {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(AccessDeniedException::class)
    fun accessDeniedException(req: HttpServletRequest, ex: AccessDeniedException): Error {
        log.error("", ex)
        return Error(
                status = 403,
                error = "Access Denied",
                message = ex.message ?: "",
                path = req.requestURI
        )
    }

    @ExceptionHandler(Exception::class)
    fun exception(req: HttpServletRequest, ex: Exception): Error {
        log.error("", ex)
        val annotation = ex.javaClass.getAnnotation(ResponseStatus::class.java)
        return Error(
                status = annotation?.code?.value() ?: 500,
                error = annotation?.code?.reasonPhrase ?: "Internal Server Error",
                message = "${annotation?.reason ?: "Error"} - ${ex.message}",
                path = req.requestURI
        )
    }

}