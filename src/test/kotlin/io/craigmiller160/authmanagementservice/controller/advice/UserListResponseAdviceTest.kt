package io.craigmiller160.authmanagementservice.controller.advice

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse

@ExtendWith(MockitoExtension::class)
class UserListResponseAdviceTest {

    @Mock
    private lateinit var method: MethodParameter
    @Mock
    private lateinit var clazz: Class<out HttpMessageConverter<*>>
    @Mock
    private lateinit var req: ServerHttpRequest
    @Mock
    private lateinit var res: ServerHttpResponse
    @Mock
    private lateinit var mediaType: MediaType

    private val userListResponseAdvice = UserListResponseAdvice()

    @Test
    fun test_supports() {
        TODO("Finish this")
    }

    @Test
    fun test_supports_wrongController() {
        TODO("Finish this")
    }

    @Test
    fun test_supports_wrongParamType() {
        TODO("Finish this")
    }

    @Test
    fun test_beforeBodyWrite() {
        TODO("Finish this")
    }

    @Test
    fun test_beforeBodyWrite_noUserList() {
        TODO("Finish this")
    }

}
