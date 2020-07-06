package io.craigmiller160.authmanagementservice.service

import io.craigmiller160.authmanagementservice.client.AuthServerClient
import io.craigmiller160.authmanagementservice.config.OAuthConfig
import io.craigmiller160.authmanagementservice.repository.ManagementRefreshTokenRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class AuthCodeServiceTest {

    @Mock
    private lateinit var oAuthConfig: OAuthConfig

    @Mock
    private lateinit var authServerClient: AuthServerClient

    @Mock
    private lateinit var manageRefreshTokenRepo: ManagementRefreshTokenRepository

    @InjectMocks
    private lateinit var authCodeService: AuthCodeService

    @Test
    fun test_prepareAuthCodeLogin() {
        TODO("Finish this")
    }

    @Test
    fun test_code() {
        TODO("Finish this")
    }

    @Test
    fun test_code_badState() {
        TODO("Finish this")
    }

    @Test
    fun test_logout() {
        TODO("Finish this")
    }

}
