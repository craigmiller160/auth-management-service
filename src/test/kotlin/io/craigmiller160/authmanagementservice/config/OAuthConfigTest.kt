package io.craigmiller160.authmanagementservice.config

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.doThrow
import com.nhaarman.mockito_kotlin.spy
import com.nimbusds.jose.jwk.JWKSet
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock

class OAuthConfigTest {

    private val oAuthConfig = OAuthConfig()

    @Test
    fun test_loadJWKSet_firstTrySuccess() {
        val spyConfig = spy(oAuthConfig)

        doReturn(1L)
                .`when`(spyConfig)
                .getBaseWait()

        doReturn(mock(JWKSet::class.java))
                .`when`(spyConfig)
                .loadJWKSet()

        spyConfig.tryToLoadJWKSet()
        assertNotNull(spyConfig.jwkSet)
    }

    @Test
    fun test_loadJWKSet_secondTrySuccess() {
        val spyConfig = spy(oAuthConfig)

        doReturn(1L)
                .`when`(spyConfig)
                .getBaseWait()

        doThrow(RuntimeException("Hello"))
                .doReturn(mock(JWKSet::class.java))
                .`when`(spyConfig)
                .loadJWKSet()

        spyConfig.tryToLoadJWKSet()
        assertNotNull(spyConfig.jwkSet)
    }

    @Test
    fun test_loadJWKSet_failure() {
        val spyConfig = spy(oAuthConfig)

        doReturn(1L)
                .`when`(spyConfig)
                .getBaseWait()

        doThrow(RuntimeException("Hello"))
                .`when`(spyConfig)
                .loadJWKSet()

        val ex = assertThrows<java.lang.RuntimeException> { spyConfig.tryToLoadJWKSet() }
        assertEquals("Failed to load JWKSet", ex.message)
        val ex2 = assertThrows<UninitializedPropertyAccessException> { spyConfig.jwkSet }
    }

}
