package io.craigmiller160.authmanagementservice.controller.advice

import io.craigmiller160.authmanagementservice.dto.ClientList
import io.craigmiller160.authmanagementservice.dto.UserList
import io.craigmiller160.authmanagementservice.entity.Client
import io.craigmiller160.authmanagementservice.entity.User
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

    private fun sanitizeEntity(entity: Any): Any {
        return when (entity) {
            is ClientList -> sanitizeClientList(entity)
            is Client -> sanitizeClient(entity)
            is UserList -> sanitizeUserList(entity)
            is User -> sanitizeUser(entity)
            else -> entity
        }
    }

    private fun sanitizeClientList(clientList: ClientList): ClientList {
        return clientList.copy(clients = clientList.clients.map { sanitizeClient(it) })
    }

    private fun sanitizeClient(client: Client): Client {
        return client.copy(clientSecret = "")
    }

    private fun sanitizeUserList(userList: UserList): UserList {
        return userList.copy(users = userList.users.map { sanitizeUser(it) })
    }

    private fun sanitizeUser(user: User): User {
        return user.copy(password = "")
    }

}
