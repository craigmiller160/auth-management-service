package io.craigmiller160.authmanagementservice.controller.advice

import io.craigmiller160.authmanagementservice.entity.Client
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@ControllerAdvice
class ClientResponseAdvice : ResponseBodyAdvice<Client> {

    override fun supports(method: MethodParameter, clazz: Class<out HttpMessageConverter<*>>): Boolean {
        return method.parameterType == Client::class.java
    }

    override fun beforeBodyWrite(client: Client?, param: MethodParameter, mediaType: MediaType, clazz: Class<out HttpMessageConverter<*>>, req: ServerHttpRequest, res: ServerHttpResponse): Client? {
        return client?.copy(clientSecret = "")
    }

}
