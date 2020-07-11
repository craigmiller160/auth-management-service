package io.craigmiller160.authmanagementservice.controller.advice

import io.craigmiller160.authmanagementservice.entity.Client
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

//@ControllerAdvice
class ResponseEntityResponseAdvice : ResponseBodyAdvice<ResponseEntity<*>> {

    // TODO write unit tests

    private val responseAdviceMap = mapOf<String,ResponseBodyAdvice<*>>(
            Client::class.java.name to ClientResponseAdvice()
    )

    override fun supports(method: MethodParameter, clazz: Class<out HttpMessageConverter<*>>): Boolean {
        println("INSIDE SUPPORTS: ${method.parameterType}") // TODO delete this
        return method.parameterType == ResponseEntity::class.java
    }

    override fun beforeBodyWrite(response: ResponseEntity<*>?, param: MethodParameter, mediaType: MediaType, clazz: Class<out HttpMessageConverter<*>>, req: ServerHttpRequest, res: ServerHttpResponse): ResponseEntity<*>? {
        println("BEFORE BODY WRITE") // TODO delete this
        return response?.let { it.body?.let { body ->
            println("Body: $body") // TODO delete this
            val advice: ResponseBodyAdvice<in Any>? = responseAdviceMap.get(body.javaClass.name) as ResponseBodyAdvice<in Any>? // TODO trying this
            if (advice != null) {
                val modified = advice.beforeBodyWrite(body, param, mediaType, clazz, req, res)
            }
            response
        } }
    }

}
