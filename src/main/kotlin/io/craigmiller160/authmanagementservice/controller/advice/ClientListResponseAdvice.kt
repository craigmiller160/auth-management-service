package io.craigmiller160.authmanagementservice.controller.advice

import io.craigmiller160.authmanagementservice.controller.BasicController
import io.craigmiller160.authmanagementservice.dto.ClientList
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@ControllerAdvice
class ClientListResponseAdvice : ResponseBodyAdvice<ClientList> {

    override fun supports(method: MethodParameter, clazz: Class<out HttpMessageConverter<*>>): Boolean {
        return method.containingClass == BasicController::class.java && method.parameterType == ClientList::class.java
    }

    override fun beforeBodyWrite(clients: ClientList?, param: MethodParameter, mediaType: MediaType, clazz: Class<out HttpMessageConverter<*>>, req: ServerHttpRequest, res: ServerHttpResponse): ClientList? {
        val newClients = clients?.clients?.map { client -> client.copy(clientSecret = "") }
        return newClients?.let { ClientList(it) }
    }

}
