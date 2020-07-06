package io.craigmiller160.authmanagementservice.client

import io.craigmiller160.authmanagementservice.config.OAuthConfig
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.web.client.RestTemplate

@ExtendWith(MockitoExtension::class)
class AuthServerClientImplTest {

    @Mock
    private lateinit var restTemplate: RestTemplate

    @Mock
    private lateinit var oAuthConfig: OAuthConfig

    @InjectMocks
    private lateinit var authServerClient: AuthServerClientImpl

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
        TODO("Finish this")
    }

}
