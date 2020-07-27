package io.craigmiller160.authmanagementservice.olddto

import io.craigmiller160.authmanagementservice.dto.Sanitizer
import io.craigmiller160.authmanagementservice.entity.User

data class FullUser (
        val user: User,
        val clients: List<FullUserClient>
) : Sanitizer<FullUser> {

    override fun sanitize(): FullUser {
        val user = this.user.sanitize()
        val clients = this.clients.map { it.sanitize() }
        return FullUser(user, clients)
    }

}
