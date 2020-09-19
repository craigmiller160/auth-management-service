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

import io.craigmiller160.authmanagementservice.entity.Client

data class UserClientDto (
        val id: Long,
        val name: String,
        val clientKey: String,
        val userId: Long,
        val allRoles: List<RoleDto> = listOf(),
        val userRoles: List<RoleDto> = listOf()
) {
    companion object {
        fun fromClient(client: Client, userId: Long): UserClientDto {
            return UserClientDto(
                    id = client.id,
                    name = client.name,
                    clientKey = client.clientKey,
                    userId = userId,
                    allRoles = listOf(),
                    userRoles = listOf()
            )
        }
    }
}
