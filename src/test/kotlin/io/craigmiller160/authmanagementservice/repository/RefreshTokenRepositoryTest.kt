/*
 *     Auth Management Service
 *     Copyright (C) 2020 Craig Miller
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.craigmiller160.authmanagementservice.repository

import graphql.kickstart.spring.web.boot.GraphQLWebsocketAutoConfiguration
import io.craigmiller160.authmanagementservice.entity.RefreshToken
import io.craigmiller160.oauth2.config.OAuthConfig
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasProperty
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.ZonedDateTime

@SpringBootTest
@ExtendWith(SpringExtension::class)
class RefreshTokenRepositoryTest {

    companion object {
        private const val CLIENT_1_ID = 1L
        private const val CLIENT_2_ID = 2L
        private const val USER_1_ID = 3L
        private const val USER_2_ID = 4L

        private val EXPIRED_TIMESTAMP: ZonedDateTime = ZonedDateTime.now().minusHours(1)
        private val TIMESTAMP: ZonedDateTime = ZonedDateTime.now()
    }

    @MockBean
    private lateinit var graphqlAutoConfig: GraphQLWebsocketAutoConfiguration
    @MockBean
    private lateinit var oAuthConfig: OAuthConfig
    @Autowired
    private lateinit var refreshTokenRepo: RefreshTokenRepository

    private lateinit var client1user1Expired: RefreshToken
    private lateinit var client1user1Valid: RefreshToken
    private lateinit var client2user1Valid: RefreshToken
    private lateinit var client1user2Valid: RefreshToken
    private lateinit var client1userNullValid: RefreshToken

    @BeforeEach
    fun beforeEach() {
        client1user1Expired = refreshTokenRepo.save(RefreshToken("Id1", "Token1", CLIENT_1_ID, USER_1_ID, EXPIRED_TIMESTAMP))
        client1user1Valid = refreshTokenRepo.save(RefreshToken("Id2", "Token2", CLIENT_1_ID, USER_1_ID, TIMESTAMP))
        client2user1Valid = refreshTokenRepo.save(RefreshToken("Id3", "Token3", CLIENT_2_ID, USER_1_ID, TIMESTAMP))
        client1user2Valid = refreshTokenRepo.save(RefreshToken("Id4", "Token4", CLIENT_1_ID, USER_2_ID, TIMESTAMP))
        client1userNullValid = refreshTokenRepo.save(RefreshToken("Id5", "Token5", CLIENT_1_ID, null, TIMESTAMP))
    }

    @AfterEach
    fun afterEach() {
        refreshTokenRepo.deleteAll()
    }

    private fun validateToken(expected: RefreshToken, actual: RefreshToken) {
        assertThat(actual, allOf(
                hasProperty("id", equalTo(expected.id)),
                hasProperty("refreshToken", equalTo(expected.refreshToken))
        ))
    }

    @Test
    fun test_findAllUserAuthentications() {
        val results = refreshTokenRepo.findAllUserAuthentications(USER_1_ID, TIMESTAMP.minusMinutes(30))
        results.sortedBy { it.id }
        assertEquals(2, results.size)
        validateToken(client1user1Valid, results[0])
        validateToken(client2user1Valid, results[1])
    }

    @Test
    fun test_findAllClientUserAuthentications() {
        val results = refreshTokenRepo.findAllClientUserAuthentications(CLIENT_1_ID, TIMESTAMP.minusMinutes(30))
        results.sortedBy { it.id }
        assertEquals(2, results.size)
        validateToken(client1user1Valid, results[0])
        validateToken(client1user2Valid, results[1])
    }

}