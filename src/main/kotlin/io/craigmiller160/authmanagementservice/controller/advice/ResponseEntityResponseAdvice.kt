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
//class ResponseEntityResponseAdvice : ResponseBodyAdvice<ResponseEntity<*>> {
//
//    // TODO write unit tests
//
//    private val <T> responseAdviceMap = mapOf<Class<T>,ResponseBodyAdvice<T>>(
//            Client::class.java to ClientResponseAdvice()
//    )
//
//    override fun supports(method: MethodParameter, clazz: Class<out HttpMessageConverter<*>>): Boolean {
//        return method.parameterType == ResponseEntity::class.java
//    }
//
//    override fun beforeBodyWrite(response: ResponseEntity<*>?, param: MethodParameter, mediaType: MediaType, clazz: Class<out HttpMessageConverter<*>>, req: ServerHttpRequest, res: ServerHttpResponse): ResponseEntity<*>? {
//        return response?.let { it.body?.let { body ->
//            val advice: ResponseBodyAdvice<*>? = responseAdviceMap.get(body.javaClass)
//            if (advice != null) {
//                val modified = advice.beforeBodyWrite(body, param, mediaType, clazz, req, res)
//            }
//            response
//        } }
//    }
//
//}
