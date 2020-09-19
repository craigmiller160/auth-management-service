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

package io.craigmiller160.authmanagementservice.dto

import io.craigmiller160.authmanagementservice.entity.User

data class ClientUserDto (
        val id: Long,
        val email: String,
        val firstName: String,
        val lastName: String,
        val clientId: Long,
        val enabled: Boolean,
        val roles: List<RoleDto> = listOf()
) {
    companion object {
        fun fromUser(user: User, clientId: Long): ClientUserDto {
            return ClientUserDto(
                    id = user.id,
                    email = user.email,
                    firstName = user.firstName,
                    lastName = user.lastName,
                    clientId = clientId,
                    enabled = user.enabled,
                    roles = listOf()
            )
        }
    }
}
