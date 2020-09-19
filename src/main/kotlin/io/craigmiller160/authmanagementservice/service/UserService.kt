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

import io.craigmiller160.authmanagementservice.dto.RoleDto
import io.craigmiller160.authmanagementservice.dto.UserClientDto
import io.craigmiller160.authmanagementservice.dto.UserDto
import io.craigmiller160.authmanagementservice.dto.UserInputDto
import io.craigmiller160.authmanagementservice.entity.ClientUser
import io.craigmiller160.authmanagementservice.entity.ClientUserRole
import io.craigmiller160.authmanagementservice.exception.EntityNotFoundException
import io.craigmiller160.authmanagementservice.repository.*
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class UserService (
        private val userRepo: UserRepository,
        private val clientRepo: ClientRepository,
        private val clientUserRoleRepo: ClientUserRoleRepository,
        private val clientUserRepo: ClientUserRepository,
        private val roleRepo: RoleRepository,
        private val refreshTokenRepo: RefreshTokenRepository
) {

    private val encoder = BCryptPasswordEncoder()

    fun getAllUsers(): List<UserDto> {
        val users = userRepo.findAllByOrderByEmail()
        return users.map { UserDto.fromUser(it) }
    }

    fun getUser(userId: Long): UserDto? {
        val user = userRepo.findById(userId).orElse(null)
        return user?.let { UserDto.fromUser(it) }
    }

    fun getClientsForUser(userId: Long): List<UserClientDto> {
        val clients = clientRepo.findAllByUserOrderByName(userId)
        return clients.map { UserClientDto.fromClient(it, userId) }
    }

    @Transactional
    fun createUser(userInput: UserInputDto): UserDto {
        val encoded = encoder.encode(userInput.password)
        val user = userInput.toUser().copy(
                password = "{bcrypt}$encoded"
        )
        val dbUser = userRepo.save(user)
        return UserDto.fromUser(dbUser)
    }

    @Transactional
    fun updateUser(userId: Long, userInput: UserInputDto): UserDto {
        val existing = userRepo.findById(userId)
                .orElseThrow { EntityNotFoundException("No user exists to update for ID: $userId") }

        val user = userInput.toUser().copy(
                id = existing.id,
                password = if (userInput.password.isNotBlank()) {
                    val encoded = encoder.encode(userInput.password)
                    "{bcrypt}$encoded"
                } else {
                    existing.password
                }
        )
        val dbUser = userRepo.save(user)
        return UserDto.fromUser(dbUser)
    }

    @Transactional
    fun deleteUser(userId: Long): UserDto {
        val existing = userRepo.findById(userId)
                .orElseThrow { EntityNotFoundException("No user exists to delete for ID: $userId") }

        clientUserRoleRepo.deleteAllByUserId(userId)
        clientUserRepo.deleteAllByUserId(userId)
        userRepo.deleteById(userId)
        refreshTokenRepo.deleteAllByUserId(userId)
        return UserDto.fromUser(existing)
    }

    @Transactional
    fun removeClientFromUser(userId: Long, clientId: Long): List<UserClientDto> {
        clientUserRoleRepo.deleteAllByUserIdAndClientId(userId, clientId)
        clientUserRepo.deleteAllByUserIdAndClientId(userId, clientId)
        refreshTokenRepo.deleteByClientIdAndUserId(clientId, userId)
        return clientRepo.findAllByUserOrderByName(userId)
                .map { UserClientDto.fromClient(it, userId) }
    }

    @Transactional
    fun addClientToUser(userId: Long, clientId: Long): List<UserClientDto> {
        val clientUser = ClientUser(0, userId, clientId)
        clientUserRepo.save(clientUser)
        return clientRepo.findAllByUserOrderByName(userId)
                .map { UserClientDto.fromClient(it, userId) }
    }

    @Transactional
    fun removeRoleFromUser(userId: Long, clientId: Long, roleId: Long): List<RoleDto> {
        clientUserRoleRepo.deleteByClientIdAndUserIdAndRoleId(clientId, userId, roleId)
        return roleRepo.findAllByClientAndUserOrderByName(clientId, userId)
                .map { RoleDto.fromRole(it) }
    }

    @Transactional
    fun addRoleToUser(userId: Long, clientId: Long, roleId: Long): List<RoleDto> {
        val clientUserRole = ClientUserRole(0, clientId, userId, roleId)
        clientUserRoleRepo.save(clientUserRole)
        return roleRepo.findAllByClientAndUserOrderByName(clientId, userId)
                .map { RoleDto.fromRole(it) }
    }

}
