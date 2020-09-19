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

package io.craigmiller160.authmanagementservice.graphql.dto

import graphql.kickstart.tools.GraphQLResolver
import io.craigmiller160.authmanagementservice.dto.RoleDto
import io.craigmiller160.authmanagementservice.dto.UserClientDto
import io.craigmiller160.authmanagementservice.service.ClientService
import org.springframework.stereotype.Component

@Component
class UserClientDtoResolver (
        private val clientService: ClientService
) : GraphQLResolver<UserClientDto> {

    fun allRoles(userClientDto: UserClientDto): List<RoleDto> {
        return clientService.getRolesForClient(userClientDto.id)
    }

    fun userRoles(userClientDto: UserClientDto): List<RoleDto> {
        return clientService.getRolesForClientAndUser(userClientDto.id, userClientDto.userId)
    }

}
