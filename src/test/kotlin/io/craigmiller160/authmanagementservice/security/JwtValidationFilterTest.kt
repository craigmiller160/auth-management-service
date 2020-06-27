package io.craigmiller160.authmanagementservice.security

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.KeyUse
import com.nimbusds.jose.jwk.RSAKey
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.core.context.SecurityContextHolder
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPublicKey

@ExtendWith(MockitoExtension::class)
class JwtValidationFilterTest {

    private lateinit var jwkSet: JWKSet
    private lateinit var jwtValidationFilter: JwtValidationFilter
    private lateinit var keyPair: KeyPair

    @BeforeEach
    fun setup() {
        val keyPairGen = KeyPairGenerator.getInstance("RSA")
        keyPair = keyPairGen.genKeyPair()


        val builder = RSAKey.Builder(keyPair.public as RSAPublicKey)
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.RS256)
                .keyID("oauth-jwt")
        jwkSet = JWKSet(builder.build())

        jwtValidationFilter = JwtValidationFilter(jwkSet)
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
