package io.craigmiller160.authmanagementservice.controller.advice

import io.craigmiller160.authmanagementservice.controller.BasicController
import io.craigmiller160.authmanagementservice.dto.UserList
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@ControllerAdvice
class UserListResponseAdvice : ResponseBodyAdvice<UserList> {
    override fun supports(method: MethodParameter, clazz: Class<out HttpMessageConverter<*>>): Boolean {
        return method.containingClass == BasicController::class.java && method.parameterType == UserList::class.java
    }

    override fun beforeBodyWrite(users: UserList?, param: MethodParameter, mediaType: MediaType, clazz: Class<out HttpMessageConverter<*>>, req: ServerHttpRequest, res: ServerHttpResponse): UserList? {
        val newUsers = users?.users?.map { user -> user.copy(password = "") }
        return newUsers?.let { UserList(it) }
    }
}