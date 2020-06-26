package io.craigmiller160.authmanagementservice.security

import com.nimbusds.jose.jwk.JWKSet
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.core.context.SecurityContextHolder

@ExtendWith(MockitoExtension::class)
class JwtValidationFilterTest {

    @Mock
    private lateinit var jwkSet: JWKSet // TODO probably can't mock this to test this class

    @InjectMocks
    private lateinit var jwtValidationFilter: JwtValidationFilter

    @BeforeEach
    fun setup() {

    }

    @AfterEach
    fun clean() {
        SecurityContextHolder.clearContext()
    }

    @Test
    fun test_doFilterInternal() {
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
