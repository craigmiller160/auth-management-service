package io.craigmiller160.authmanagementservice.dto

import io.craigmiller160.authmanagementservice.entity.Role

data class RoleInputDto (
        val name: String,
        val clientId: Long
) {
    fun toRole(): Role {
        return Role(
                id = 0,
                name = this.name,
                clientId = this.clientId
        )
    }
}
