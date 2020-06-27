package io.craigmiller160.authmanagementservice.controller.advice

import io.craigmiller160.authmanagementservice.exception.InvalidTokenException
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.access.AccessDeniedException
import javax.servlet.http.HttpServletRequest

@ExtendWith(MockitoExtension::class)
class ErrorControllerAdviceTest {

    @Mock
    private lateinit var req: HttpServletRequest

    private val errorControllerAdvice = ErrorControllerAdvice()

    @Test
    fun test_exception_withAnnotation() {
        Mockito.`when`(req.requestURI).thenReturn("uri")
        val ex = InvalidTokenException("message")

        val error = errorControllerAdvice.exception(req, ex)
        MatcherAssert.assertThat(error, CoreMatchers.allOf(
                Matchers.hasProperty("status", CoreMatchers.equalTo(401)),
                Matchers.hasProperty("error", CoreMatchers.equalTo("Unauthorized")),
                Matchers.hasProperty("message", CoreMatchers.equalTo("Unauthorized - message")),
                Matchers.hasProperty("timestamp", CoreMatchers.notNullValue()),
                Matchers.hasProperty("path", CoreMatchers.equalTo("uri"))
        ))
    }

    @Test
    fun test_exception() {
        Mockito.`when`(req.requestURI).thenReturn("uri")
        val ex = Exception("message")

        val error = errorControllerAdvice.exception(req, ex)
        MatcherAssert.assertThat(error, CoreMatchers.allOf(
                Matchers.hasProperty("status", CoreMatchers.equalTo(500)),
                Matchers.hasProperty("error", CoreMatchers.equalTo("Internal Server Error")),
                Matchers.hasProperty("message", CoreMatchers.equalTo("Error - message")),
                Matchers.hasProperty("timestamp", CoreMatchers.notNullValue()),
                Matchers.hasProperty("path", CoreMatchers.equalTo("uri"))
        ))
    }

    @Test
    fun test_accessDeniedException() {
        Mockito.`when`(req.requestURI).thenReturn("uri")
        val ex = Mockito.mock(AccessDeniedException::class.java)
        Mockito.`when`(ex.message).thenReturn("message")

        val error = errorControllerAdvice.accessDeniedException(req, ex)
        MatcherAssert.assertThat(error, CoreMatchers.allOf(
                Matchers.hasProperty("status", CoreMatchers.equalTo(403)),
                Matchers.hasProperty("error", CoreMatchers.equalTo("Access Denied")),
                Matchers.hasProperty("message", CoreMatchers.equalTo("message")),
                Matchers.hasProperty("timestamp", CoreMatchers.notNullValue()),
                Matchers.hasProperty("path", CoreMatchers.equalTo("uri"))
        ))
    }

}
