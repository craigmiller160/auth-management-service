package io.craigmiller160.authmanagementservice.integration

import com.fasterxml.jackson.databind.ObjectMapper
import io.craigmiller160.authmanagementservice.entity.Client
import io.craigmiller160.oauth2.config.OAuthConfig
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@ExtendWith(SpringExtension::class)
@AutoConfigureMockMvc
class ClientControllerIntegrationTest {

    @MockBean
    private lateinit var oauthConfig: OAuthConfig

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun test_createClient() {
        val client = Client(
                id = 0,
                name = "Client",
                clientKey = "key",
                clientSecret = "secret",
                enabled = true,
                allowAuthCode = true,
                allowPassword = true,
                allowClientCredentials = true,
                accessTokenTimeoutSecs = 100,
                refreshTokenTimeoutSecs = 100
        )
        mockMvc.perform(
                post("/clients")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(client))
                        .secure(true)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)
    }

}
