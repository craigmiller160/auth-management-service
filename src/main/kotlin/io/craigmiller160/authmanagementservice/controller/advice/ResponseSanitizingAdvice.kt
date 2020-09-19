/*
 *     Auth Management Service
 *     Copyright (C) 2020 Craig Miller
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
