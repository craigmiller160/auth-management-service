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
import io.craigmiller160.authmanagementservice.dto.ClientDto
import io.craigmiller160.authmanagementservice.dto.ClientInputDto
import io.craigmiller160.authmanagementservice.dto.ClientUserDto
import io.craigmiller160.authmanagementservice.service.ClientService
import org.springframework.stereotype.Component

@Component
class ClientMutationResolver (
        private val clientService: ClientService
) : GraphQLMutationResolver {

    fun createClient(client: ClientInputDto): ClientDto {
        return clientService.createClient(client)
    }

    fun updateClient(clientId: Long, client: ClientInputDto): ClientDto {
        return clientService.updateClient(clientId, client)
    }

    fun deleteClient(clientId: Long): ClientDto {
        return clientService.deleteClient(clientId)
    }

    fun removeUserFromClient(userId: Long, clientId: Long): List<ClientUserDto> {
        return clientService.removeUserFromClient(userId, clientId)
    }

    fun addUserToClient(userId: Long, clientId: Long): List<ClientUserDto> {
        return clientService.addClientToUser(userId, clientId)
    }

}
