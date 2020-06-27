package io.craigmiller160.authmanagementservice.security

import com.nimbusds.jose.jwk.JWKSet
import io.craigmiller160.authmanagementservice.testutils.JwtUtils
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.core.context.SecurityContextHolder
import java.security.KeyPair

@ExtendWith(MockitoExtension::class)
class JwtValidationFilterTest {

    private lateinit var jwkSet: JWKSet
    private lateinit var jwtValidationFilter: JwtValidationFilter
    private lateinit var keyPair: KeyPair

    @BeforeEach
    fun setup() {
        keyPair = JwtUtils.createKeyPair()
        jwkSet = JwtUtils.createJwkSet(keyPair)

//        jwtValidationFilter = JwtValidationFilter(jwkSet)
    }

    @AfterEach
    fun clean() {
        SecurityContextHolder.clearContext()
    }

    @Test
    fun test_doFilterInternal_authHeader() {
        TODO("Finish this")
    }

    @Test
    fun test_doFilterInternal_cookie() {
        TODO("Finish this")
    }

    @Test
    fun test_doFilterInternal_fail() {
        TODO("Finish this")
    }

    @Test
    fun test_doFilterInternal_notBearer() {
        TODO("Finish this")
    }

}
