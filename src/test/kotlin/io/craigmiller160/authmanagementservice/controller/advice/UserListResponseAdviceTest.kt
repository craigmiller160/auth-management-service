package io.craigmiller160.authmanagementservice.controller.advice

import io.craigmiller160.authmanagementservice.controller.BasicController
import io.craigmiller160.authmanagementservice.dto.UserList
import io.craigmiller160.authmanagementservice.entity.User
import io.craigmiller160.authmanagementservice.testutils.TestData
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
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
    private val clazz: Class<out HttpMessageConverter<*>> = HttpMessageConverter::class.java
    @Mock
    private lateinit var req: ServerHttpRequest
    @Mock
    private lateinit var res: ServerHttpResponse
    @Mock
    private lateinit var mediaType: MediaType

    private val userListResponseAdvice = UserListResponseAdvice()
    private val userList = TestData.createUserList()

    @Test
    fun test_supports() {
        `when`(method.containingClass)
                .thenReturn(BasicController::class.java)
        `when`(method.parameterType)
                .thenReturn(UserList::class.java)

        val result = userListResponseAdvice.supports(method, clazz)
        assertTrue(result)
    }

    @Test
    fun test_supports_wrongController() {
        `when`(method.containingClass)
                .thenReturn(String::class.java)

        val result = userListResponseAdvice.supports(method, clazz)
        assertFalse(result)
    }

    @Test
    fun test_supports_wrongParamType() {
        `when`(method.containingClass)
                .thenReturn(BasicController::class.java)
        `when`(method.parameterType)
                .thenReturn(String::class.java)

        val result = userListResponseAdvice.supports(method, clazz)
        assertFalse(result)
    }

    @Test
    fun test_beforeBodyWrite() {
        val result = userListResponseAdvice.beforeBodyWrite(userList, method, mediaType, clazz, req, res)
        assertNotNull(result)
        assertNotNull(result?.users)
        assertEquals(1, result?.users?.size)
        assertEquals(userList.users[0].copy(password = ""), result?.users?.getOrNull(0))
    }

    @Test
    fun test_beforeBodyWrite_noUserList() {
        val result = userListResponseAdvice.beforeBodyWrite(null, method, mediaType, clazz, req, res)
        assertNull(result)
    }

}
