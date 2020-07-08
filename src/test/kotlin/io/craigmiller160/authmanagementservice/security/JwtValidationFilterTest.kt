package io.craigmiller160.authmanagementservice.security

import com.nimbusds.jose.jwk.JWKSet
import io.craigmiller160.authmanagementservice.entity.ManagementRefreshToken
import io.craigmiller160.authmanagementservice.repository.ManagementRefreshTokenRepository
import io.craigmiller160.authmanagementservice.testutils.JwtUtils
import io.craigmiller160.oauth2.client.AuthServerClient
import io.craigmiller160.oauth2.config.OAuthConfig
import io.craigmiller160.oauth2.dto.TokenResponse
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import java.security.KeyPair
import javax.servlet.FilterChain
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@ExtendWith(MockitoExtension::class)
class JwtValidationFilterTest {

    private lateinit var oAuthConfig: OAuthConfig
    private lateinit var jwkSet: JWKSet
    private lateinit var jwtValidationFilter: JwtValidationFilter
    private lateinit var keyPair: KeyPair
    private lateinit var token: String
    private val cookieName = "cookie"

    @Mock
    private lateinit var authServerClient: AuthServerClient
    @Mock
    private lateinit var manageRefreshTokenRepo: ManagementRefreshTokenRepository
    @Mock
    private lateinit var req: HttpServletRequest
    @Mock
    private lateinit var res: HttpServletResponse
    @Mock
    private lateinit var chain: FilterChain

    @BeforeEach
    fun setup() {
        keyPair = JwtUtils.createKeyPair()
        jwkSet = JwtUtils.createJwkSet(keyPair)
        oAuthConfig = OAuthConfig(
                clientKey = JwtUtils.CLIENT_KEY,
                clientName = JwtUtils.CLIENT_NAME,
                acceptBearerToken = true,
                acceptCookie = true,
                cookieName = cookieName
        )
        oAuthConfig.jwkSet = jwkSet

        val jwt = JwtUtils.createJwt()
        token = JwtUtils.signAndSerializeJwt(jwt, keyPair.private)

        jwtValidationFilter = JwtValidationFilter(oAuthConfig, manageRefreshTokenRepo, authServerClient)
        `when`(req.requestURI)
                .thenReturn("/something")
    }

    @AfterEach
    fun clean() {
        SecurityContextHolder.clearContext()
    }

    @Test
    fun test_doFilterInternal_validBearerToken() {
        `when`(req.getHeader("Authorization"))
                .thenReturn("Bearer $token")

        jwtValidationFilter.doFilter(req, res, chain)
        val authentication = SecurityContextHolder.getContext().authentication
        assertNotNull(authentication)
        val principal = authentication.principal as UserDetails
        assertEquals(JwtUtils.USERNAME, principal.username)
        assertEquals(SimpleGrantedAuthority(JwtUtils.ROLE_1), authentication.authorities.toList()[0])
        assertEquals(SimpleGrantedAuthority(JwtUtils.ROLE_2), authentication.authorities.toList()[1])
        verify(chain, times(1))
                .doFilter(req, res)
    }

    @Test
    fun test_doFilterInternal_noToken() {
        jwtValidationFilter.doFilter(req, res, chain)
        assertNull(SecurityContextHolder.getContext().authentication)
        verify(chain, times(1))
                .doFilter(req, res)
    }

    @Test
    fun test_doFilterInternal_cookie() {
        val cookie = Cookie(cookieName, token)
        `when`(req.cookies)
                .thenReturn(arrayOf(cookie))

        jwtValidationFilter.doFilter(req, res, chain)
        val authentication = SecurityContextHolder.getContext().authentication
        assertNotNull(authentication)
        val principal = authentication.principal as UserDetails
        assertEquals(JwtUtils.USERNAME, principal.username)
        assertEquals(SimpleGrantedAuthority(JwtUtils.ROLE_1), authentication.authorities.toList()[0])
        assertEquals(SimpleGrantedAuthority(JwtUtils.ROLE_2), authentication.authorities.toList()[1])
        verify(chain, times(1))
                .doFilter(req, res)
    }

    @Test
    fun test_doFilterInternal_authcodeUri() {
        `when`(req.requestURI).thenReturn("/authcode/foo")

        jwtValidationFilter.doFilter(req, res, chain)
        verify(chain, times(1))
                .doFilter(req, res)
    }

    @Test
    fun test_doFilterInternal_badSignature() {
        val keyPair = JwtUtils.createKeyPair()
        val jwt = JwtUtils.createJwt()
        val token = JwtUtils.signAndSerializeJwt(jwt, keyPair.private)
        `when`(req.getHeader("Authorization"))
                .thenReturn("Bearer $token")

        jwtValidationFilter.doFilter(req, res, chain)
        assertNull(SecurityContextHolder.getContext().authentication)
        verify(chain, times(1))
                .doFilter(req, res)
    }

    @Test
    fun test_doFilterInternal_wrongClient() {
        oAuthConfig.clientKey = "ABCDEFG"
        `when`(req.getHeader("Authorization"))
                .thenReturn("Bearer $token")

        jwtValidationFilter.doFilter(req, res, chain)
        assertNull(SecurityContextHolder.getContext().authentication)
        verify(chain, times(1))
                .doFilter(req, res)
    }

    @Test
    fun test_doFilterInternal_expired() {
        val jwt = JwtUtils.createJwt(-20)
        val token = JwtUtils.signAndSerializeJwt(jwt, keyPair.private)
        `when`(req.getHeader("Authorization"))
                .thenReturn("Bearer $token")

        jwtValidationFilter.doFilter(req, res, chain)
        assertNull(SecurityContextHolder.getContext().authentication)
        verify(chain, times(1))
                .doFilter(req, res)
    }

    @Test
    fun test_doFilterInternal_notBearer() {
        `when`(req.getHeader("Authorization"))
                .thenReturn(token)

        jwtValidationFilter.doFilter(req, res, chain)
        assertNull(SecurityContextHolder.getContext().authentication)
        verify(chain, times(1))
                .doFilter(req, res)
    }

    @Test
    fun test_doFilterInternal_refresh() {
        val jwt = JwtUtils.createJwt(-20)
        val token = JwtUtils.signAndSerializeJwt(jwt, keyPair.private)
        `when`(req.getHeader("Authorization"))
                .thenReturn("Bearer $token")

        val refreshToken = "ABCDEFG"
        val newRefreshToken = "HIJKLMNO"
        val newTokenId = "id2"

        `when`(manageRefreshTokenRepo.findByTokenId(JwtUtils.TOKEN_ID))
                .thenReturn(ManagementRefreshToken(1, JwtUtils.TOKEN_ID, refreshToken))
        doReturn(TokenResponse(this.token, newRefreshToken, newTokenId))
                .`when`(authServerClient)
                .authenticateRefreshToken(refreshToken)

        jwtValidationFilter.doFilter(req, res, chain)
        val authentication = SecurityContextHolder.getContext().authentication
        assertNotNull(authentication)
        val principal = authentication.principal as UserDetails
        assertEquals(JwtUtils.USERNAME, principal.username)
        assertEquals(SimpleGrantedAuthority(JwtUtils.ROLE_1), authentication.authorities.toList()[0])
        assertEquals(SimpleGrantedAuthority(JwtUtils.ROLE_2), authentication.authorities.toList()[1])

        verify(chain, times(1))
                .doFilter(req, res)
        verify(manageRefreshTokenRepo, times(1))
                .deleteById(1)
        verify(manageRefreshTokenRepo, times(1))
                .save(ManagementRefreshToken(0, newTokenId, newRefreshToken))
    }

}
