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

package io.craigmiller160.authmanagementservice.graphql.mutation

import graphql.kickstart.tools.GraphQLMutationResolver
import io.craigmiller160.authmanagementservice.dto.RoleDto
import io.craigmiller160.authmanagementservice.dto.UserClientDto
import io.craigmiller160.authmanagementservice.dto.UserDto
import io.craigmiller160.authmanagementservice.dto.UserInputDto
import io.craigmiller160.authmanagementservice.service.UserService
import org.springframework.stereotype.Component

@Component
class UserMutationResolver (
        private val userService: UserService
) : GraphQLMutationResolver {

    fun createUser(userInput: UserInputDto): UserDto {
        return userService.createUser(userInput)
    }

    fun updateUser(userId: Long, userInput: UserInputDto): UserDto {
        return userService.updateUser(userId, userInput)
    }

    fun deleteUser(userId: Long): UserDto {
        return userService.deleteUser(userId)
    }

    fun removeClientFromUser(userId: Long, clientId: Long): List<UserClientDto> {
        return userService.removeClientFromUser(userId, clientId)
    }

    fun addClientToUser(userId: Long, clientId: Long): List<UserClientDto> {
        return userService.addClientToUser(userId, clientId)
    }

    fun removeRoleFromUser(userId: Long, clientId: Long, roleId: Long): List<RoleDto> {
        return userService.removeRoleFromUser(userId, clientId, roleId)
    }

    fun addRoleToUser(userId: Long, clientId: Long, roleId: Long): List<RoleDto> {
        return userService.addRoleToUser(userId, clientId, roleId)
    }

}
