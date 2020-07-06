package io.craigmiller160.authmanagementservice.controller

import io.craigmiller160.authmanagementservice.client.AuthServerClient
import io.craigmiller160.authmanagementservice.config.OAuthConfig
import io.craigmiller160.authmanagementservice.config.WebSecurityConfig
import io.craigmiller160.authmanagementservice.repository.ManagementRefreshTokenRepository
import io.craigmiller160.authmanagementservice.security.AuthEntryPoint
import io.craigmiller160.authmanagementservice.security.JwtFilterConfigurer
import io.craigmiller160.authmanagementservice.service.AuthCodeService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc

@WebMvcTest
@ContextConfiguration(classes = [
    JwtFilterConfigurer::class,
    AuthCodeController::class,
    WebSecurityConfig::class,
    AuthEntryPoint::class
])
class AuthCodeControllerTest {

    @MockBean
    private lateinit var oAuthConfig: OAuthConfig

    @MockBean
    private lateinit var manageRefreshTokenRepo: ManagementRefreshTokenRepository

    @MockBean
    private lateinit var authServerClient: AuthServerClient

    @MockBean
    private lateinit var authCodeService: AuthCodeService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var authCodeController: AuthCodeController

    @Test
    fun test_login() {
        TODO("Finish this")
    }

    @Test
    fun test_code() {
        TODO("Finish this")
    }

    @Test
    fun test_logout() {
        TODO("Finish this")
    }

}
