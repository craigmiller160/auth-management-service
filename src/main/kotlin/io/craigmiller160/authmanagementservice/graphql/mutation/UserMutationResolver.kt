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

    fun removeRoleFromUser(userId: Long, roleId: Long): List<RoleDto> {
        TODO("Finish this")
    }

    fun addRoleToUser(userId: Long, roleId: Long): List<RoleDto> {
        TODO("Finish this")
    }

}
