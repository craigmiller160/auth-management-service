package io.craigmiller160.authmanagementservice.service

import io.craigmiller160.oauth2.client.AuthServerClient
import io.craigmiller160.oauth2.repository.AppRefreshTokenRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class TokenRefreshServiceTest {

    @Mock
    private lateinit var appRefreshTokenRepo: AppRefreshTokenRepository

    @Mock
    private lateinit var authServerClient: AuthServerClient

    @InjectMocks
    private lateinit var tokenRefreshService: TokenRefreshService

    @Test
    fun test_refreshToken() {
        TODO("Finish this")
    }

    @Test
    fun test_refreshToken_notFound() {
        TODO("Finish this")
    }

    @Test
    fun test_refreshToken_exception() {
        TODO("Finish this")
    }

}
