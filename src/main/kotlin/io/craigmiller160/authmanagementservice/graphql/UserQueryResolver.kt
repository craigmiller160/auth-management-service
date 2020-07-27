package io.craigmiller160.authmanagementservice.graphql

import graphql.kickstart.tools.GraphQLQueryResolver
import io.craigmiller160.authmanagementservice.dto.UserDto
import io.craigmiller160.authmanagementservice.service.UserService
import org.springframework.stereotype.Component

@Component
class UserQueryResolver (
        private val userService: UserService
) : GraphQLQueryResolver {

    fun getUsers(): List<UserDto> {
        return userService.getUsers2()
    }

    fun getUser(id: Long): UserDto? {
        return userService.getUser2(id)
    }

}
