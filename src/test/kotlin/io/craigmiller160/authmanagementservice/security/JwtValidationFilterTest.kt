package io.craigmiller160.authmanagementservice.security

import com.nimbusds.jose.jwk.JWKSet
import io.craigmiller160.authmanagementservice.config.OAuthConfig
import io.craigmiller160.authmanagementservice.testutils.JwtUtils
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import java.security.KeyPair
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@ExtendWith(MockitoExtension::class)
class JwtValidationFilterTest {

    private lateinit var OAuthConfig: OAuthConfig
    private lateinit var jwkSet: JWKSet
    private lateinit var jwtValidationFilter: JwtValidationFilter
    private lateinit var keyPair: KeyPair
    private lateinit var token: String

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
        OAuthConfig = OAuthConfig(
                clientKey = JwtUtils.CLIENT_KEY,
                clientName = JwtUtils.CLIENT_NAME
        )
        OAuthConfig.jwkSet = jwkSet

        val jwt = JwtUtils.createJwt()
        token = JwtUtils.signAndSerializeJwt(jwt, keyPair.private)

        jwtValidationFilter = JwtValidationFilter(OAuthConfig)
    }

    @AfterEach
    fun clean() {
        SecurityContextHolder.clearContext()
    }

    @Test
    fun test_doFilterInternal_authHeader() {
        `when`(req.getHeader("Authorization"))
                .thenReturn("Bearer $token")

        jwtValidationFilter.doFilter(req, res, chain)
        val authentication = SecurityContextHolder.getContext().authentication
        assertNotNull(authentication)
        val principal = authentication.principal as UserDetails
        assertEquals(JwtUtils.USERNAME, principal.username)
        assertEquals(SimpleGrantedAuthority(JwtUtils.ROLE_1), authentication.authorities.toList()[0])
        assertEquals(SimpleGrantedAuthority(JwtUtils.ROLE_2), authentication.authorities.toList()[1])
    }

    @Test
    fun test_doFilterInternal_cookie() {
        TODO("Finish this")
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
    }

    @Test
    fun test_doFilterInternal_wrongClient() {
        OAuthConfig.clientKey = "ABCDEFG"
        `when`(req.getHeader("Authorization"))
                .thenReturn("Bearer $token")

        jwtValidationFilter.doFilter(req, res, chain)
        assertNull(SecurityContextHolder.getContext().authentication)
    }

    @Test
    fun test_doFilterInternal_expired() {
        val jwt = JwtUtils.createJwt(-20)
        val token = JwtUtils.signAndSerializeJwt(jwt, keyPair.private)
        `when`(req.getHeader("Authorization"))
                .thenReturn("Bearer $token")

        jwtValidationFilter.doFilter(req, res, chain)
        assertNull(SecurityContextHolder.getContext().authentication)
    }

    @Test
    fun test_doFilterInternal_notBearer() {
        `when`(req.getHeader("Authorization"))
                .thenReturn(token)

        jwtValidationFilter.doFilter(req, res, chain)
        assertNull(SecurityContextHolder.getContext().authentication)
    }

}
