package io.craigmiller160.authmanagementservice.client

import io.craigmiller160.authmanagementservice.config.OAuthConfig
import io.craigmiller160.authmanagementservice.dto.TokenResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.eq
import org.mockito.ArgumentMatchers.isA
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

@ExtendWith(MockitoExtension::class)
class AuthServerClientImplTest {

    private val host = "host"
    private val path = "path"
    private val key = "key"
    private val secret = "secret"
    private val redirectUri = "redirectUri"
    private val authHeader = "Basic a2V5OnNlY3JldA=="
    private val response = TokenResponse("access", "refresh", "id")

    @Mock
    private lateinit var restTemplate: RestTemplate

    @Mock
    private lateinit var oAuthConfig: OAuthConfig

    @InjectMocks
    private lateinit var authServerClient: AuthServerClientImpl

    @BeforeEach
    fun setup() {
        `when`(oAuthConfig.authServerHost)
                .thenReturn(host)
        `when`(oAuthConfig.tokenPath)
                .thenReturn(path)
        `when`(oAuthConfig.clientKey)
                .thenReturn(key)
        `when`(oAuthConfig.clientSecret)
                .thenReturn(secret)
    }

    @Test
    fun test_authenticateAuthCode() {
        TODO("Finish this")
    }

    @Test
    fun test_authenticateAuthCode_invalidResponseBody() {
        TODO("Finish this")
    }

    @Test
    fun test_authenticateRefreshToken() {
        val refreshToken = "ABCDEFG"

        val entityCaptor = ArgumentCaptor.forClass(HttpEntity::class.java)

        `when`(restTemplate.exchange(
                eq("$host$path"),
                eq(HttpMethod.POST),
                entityCaptor.capture(),
                eq(TokenResponse::class.java)
        ))
                .thenReturn(ResponseEntity.ok(response))

        val result = authServerClient.authenticateRefreshToken(refreshToken)
        assertEquals(response, result)

        assertEquals(1, entityCaptor.allValues.size)
        val entity = entityCaptor.value

        assertEquals(this.authHeader, entity.headers["Authorization"]?.get(0))
        assertEquals(MediaType.APPLICATION_FORM_URLENCODED_VALUE, entity.headers["Content-Type"]?.get(0))

        val body = entity.body
        assertTrue(body is MultiValueMap<*,*>)
        val map = body as MultiValueMap<String,String>
        assertEquals("refresh_token", map["grant_type"]?.get(0))
        assertEquals(refreshToken, map["refresh_token"]?.get(0))
    }

}
