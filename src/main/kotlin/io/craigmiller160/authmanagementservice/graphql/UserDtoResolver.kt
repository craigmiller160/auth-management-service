package io.craigmiller160.authmanagementservice.graphql

import graphql.kickstart.tools.GraphQLResolver
import io.craigmiller160.authmanagementservice.dto.UserClientDto
import io.craigmiller160.authmanagementservice.dto.UserDto
import io.craigmiller160.authmanagementservice.service.LegacyUserService
import org.springframework.stereotype.Component

@Component
class UserDtoResolver (
        private val legacyUserService: LegacyUserService
) : GraphQLResolver<UserDto> {

    fun clients(user: UserDto): List<UserClientDto> {
        return legacyUserService.getUserClients2(user.id)
    }

}
