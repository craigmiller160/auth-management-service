package io.craigmiller160.authmanagementservice.security

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class AuthEntryPointTest {

    @Mock
    private lateinit var objectMapper: ObjectMapper

    @InjectMocks
    private lateinit var authEntryPoint: AuthEntryPoint

    @Test
    fun test_alreadyErrorStatus() {
        TODO("Finish this")
    }

    @Test
    fun test_setUnauthorized() {
        TODO("Finish this")
    }

}
