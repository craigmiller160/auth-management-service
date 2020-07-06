package io.craigmiller160.authmanagementservice.service

import io.craigmiller160.authmanagementservice.client.AuthServerClient
import io.craigmiller160.authmanagementservice.config.OAuthConfig
import io.craigmiller160.authmanagementservice.repository.ManagementRefreshTokenRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.eq
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

@ExtendWith(MockitoExtension::class)
class AuthCodeServiceTest {

    private val host = "host"
    private val path = "/path"
    private val redirectUri = "redirectUri"
    private val clientKey = "clientKey"

    @Mock
    private lateinit var oAuthConfig: OAuthConfig

    @Mock
    private lateinit var authServerClient: AuthServerClient

    @Mock
    private lateinit var manageRefreshTokenRepo: ManagementRefreshTokenRepository

    @Mock
    private lateinit var req: HttpServletRequest

    @Mock
    private lateinit var session: HttpSession

    @InjectMocks
    private lateinit var authCodeService: AuthCodeService

    @BeforeEach
    fun setup() {
        `when`(req.session)
                .thenReturn(session)
    }

    @Test
    fun test_prepareAuthCodeLogin() {
        `when`(oAuthConfig.authServerHost)
                .thenReturn(host)
        `when`(oAuthConfig.authCodeLoginPath)
                .thenReturn(path)
        `when`(oAuthConfig.authCodeRedirectUri)
                .thenReturn(redirectUri)
        `when`(oAuthConfig.clientKey)
                .thenReturn(clientKey)

        val result = authCodeService.prepareAuthCodeLogin(req)

        val captor = ArgumentCaptor.forClass(String::class.java)

        verify(session, times(1))
                .setAttribute(eq(AuthCodeService.STATE_ATTR), captor.capture())

        assertNotNull(captor.value)
        val state = captor.value

        val expected = "$host$path?response_type=code&client_id=$clientKey&redirect_uri=$redirectUri&state=$state"
        assertEquals(expected, result)
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
