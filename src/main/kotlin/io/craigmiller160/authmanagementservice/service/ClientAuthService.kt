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

package io.craigmiller160.authmanagementservice.service

import io.craigmiller160.authmanagementservice.dto.ClientAuthDetailsDto
import io.craigmiller160.authmanagementservice.dto.UserAuthDetailsDto
import io.craigmiller160.authmanagementservice.entity.RefreshToken
import io.craigmiller160.authmanagementservice.exception.EntityNotFoundException
import io.craigmiller160.authmanagementservice.repository.ClientRepository
import io.craigmiller160.authmanagementservice.repository.RefreshTokenRepository
import io.craigmiller160.authmanagementservice.repository.UserRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class ClientAuthService (
        private val clientRepo: ClientRepository,
        private val userRepo: UserRepository,
        private val refreshTokenRepo: RefreshTokenRepository
) {

    @Transactional
    fun getAuthDetailsForClientUsers(clientId: Long): ClientAuthDetailsDto {
        val client = clientRepo.findById(clientId)
                .orElseThrow { EntityNotFoundException("No client for ID $clientId") }

        val authDetails = refreshTokenRepo.findByClientIdAndUserIdIsNotNull(clientId)
                .fold(mapOf<Long,RefreshToken>()) { map, token ->
                    val existingToken = map[token.userId]
                    if (existingToken != null && existingToken.timestamp < token.timestamp) {
                        map + mapOf(token.userId!! to token)
                    } else {
                        map + mapOf(token.userId!! to token)
                    }
                }
                .values
                .map {
                    val user = userRepo.findById(it.userId!!)
                            .orElseThrow { EntityNotFoundException("No user for ID: ${it.userId}") }
                    UserAuthDetailsDto(
                            clientId = clientId,
                            clientName = client.name,
                            userId = it.userId ?: 0,
                            userEmail = user.email,
                            lastAuthenticated = it.timestamp
                    )
                }
        return ClientAuthDetailsDto(
                clientName = client.name,
                userAuthDetails = authDetails
        )
    }

}
