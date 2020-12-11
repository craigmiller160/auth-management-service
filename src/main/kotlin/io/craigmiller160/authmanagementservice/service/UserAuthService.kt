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

import io.craigmiller160.authmanagementservice.dto.UserAuthDetailsDto
import io.craigmiller160.authmanagementservice.dto.UserAuthDetailsListDto
import io.craigmiller160.authmanagementservice.exception.EntityNotFoundException
import io.craigmiller160.authmanagementservice.repository.ClientRepository
import io.craigmiller160.authmanagementservice.repository.RefreshTokenRepository
import io.craigmiller160.authmanagementservice.repository.UserRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class UserAuthService (
        private val refreshTokenRepo: RefreshTokenRepository,
        private val userRepo: UserRepository,
        private val clientRepo: ClientRepository
) {

    @Transactional
    fun getAllUserAuthDetails(userId: Long): UserAuthDetailsListDto {
        val user = userRepo.findById(userId)
                .orElseThrow { EntityNotFoundException("No user found for id $userId") }

        val authDetails = refreshTokenRepo.findAllByUserId(userId)
                .map {
                    val client = clientRepo.findById(it.clientId)
                            .orElseThrow { EntityNotFoundException("No client found for id ${it.clientId}") }
                    UserAuthDetailsDto(
                            clientId = client.id,
                            clientName = client.name,
                            userId = userId,
                            userEmail = user.email,
                            lastAuthenticated = it.timestamp
                    )
                }
        return UserAuthDetailsListDto(user.email, authDetails)
    }

    @Transactional
    fun revokeUserAuthAccess(clientId: Long, userId: Long) {
        val user = userRepo.findByClientAndUser(clientId, userId)
                ?: throw EntityNotFoundException("No auth details found for client $clientId and user $userId")
        val client = clientRepo.findById(clientId)
                .orElseThrow { EntityNotFoundException("No client found for id $clientId") }

        refreshTokenRepo.deleteByClientIdAndUserId(clientId, userId)
    }

}
