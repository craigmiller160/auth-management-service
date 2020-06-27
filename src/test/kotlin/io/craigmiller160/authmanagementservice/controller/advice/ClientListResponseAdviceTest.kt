package io.craigmiller160.authmanagementservice.controller.advice

import io.craigmiller160.authmanagementservice.controller.BasicController
import io.craigmiller160.authmanagementservice.dto.ClientList
import io.craigmiller160.authmanagementservice.dto.UserList
import io.craigmiller160.authmanagementservice.entity.Client
import io.craigmiller160.authmanagementservice.testutils.TestData
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse

@ExtendWith(MockitoExtension::class)
class ClientListResponseAdviceTest {

    @Mock
    private lateinit var method: MethodParameter
    private val clazz: Class<out HttpMessageConverter<*>> = HttpMessageConverter::class.java
    @Mock
    private lateinit var req: ServerHttpRequest
    @Mock
    private lateinit var res: ServerHttpResponse
    @Mock
    private lateinit var mediaType: MediaType

    private val clientResponseAdvice = ClientListResponseAdvice()
    private val clientList = TestData.createClientList()

    @Test
    fun test_supports() {
        Mockito.`when`(method.containingClass)
                .thenReturn(BasicController::class.java)
        Mockito.`when`(method.parameterType)
                .thenReturn(ClientList::class.java)

        val result = clientResponseAdvice.supports(method, clazz)
        Assertions.assertTrue(result)
    }

    @Test
    fun test_supports_wrongController() {
        Mockito.`when`(method.containingClass)
                .thenReturn(String::class.java)

        val result = clientResponseAdvice.supports(method, clazz)
        Assertions.assertFalse(result)
    }

    @Test
    fun test_supports_wrongParamType() {
        Mockito.`when`(method.containingClass)
                .thenReturn(BasicController::class.java)
        Mockito.`when`(method.parameterType)
                .thenReturn(String::class.java)

        val result = clientResponseAdvice.supports(method, clazz)
        Assertions.assertFalse(result)
    }

    @Test
    fun test_beforeBodyWrite() {
        val result = clientResponseAdvice.beforeBodyWrite(clientList, method, mediaType, clazz, req, res)
        Assertions.assertNotNull(result)
        Assertions.assertNotNull(result?.clients)
        Assertions.assertEquals(1, result?.clients?.size)
        Assertions.assertEquals(clientList.clients[0].copy(clientSecret = ""), result?.clients?.getOrNull(0))
    }

    @Test
    fun test_beforeBodyWrite_noClientList() {
        val result = clientResponseAdvice.beforeBodyWrite(null, method, mediaType, clazz, req, res)
        Assertions.assertNull(result)
    }

}
