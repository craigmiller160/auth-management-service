package io.craigmiller160.authmanagementservice.graphql.query

import graphql.kickstart.tools.GraphQLQueryResolver
import io.craigmiller160.authmanagementservice.dto.UserDto
import io.craigmiller160.authmanagementservice.service.LegacyUserService
import org.springframework.stereotype.Component

@Component
class UserQueryResolver (
        private val legacyUserService: LegacyUserService
) : GraphQLQueryResolver {

    fun getUsers(): List<UserDto> {
        return legacyUserService.getUsers2()
    }

    fun getUser(id: Long): UserDto? {
        return legacyUserService.getUser2(id)
    }

}
