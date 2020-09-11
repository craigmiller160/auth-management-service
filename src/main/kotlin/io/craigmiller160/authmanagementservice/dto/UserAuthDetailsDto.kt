package io.craigmiller160.authmanagementservice.dto

import java.time.LocalDateTime

data class UserAuthDetailsDto (
        val tokenId: String?, // TODO not null
        val clientId: Long,
        val clientName: String,
        val userId: Long,
        val userEmail: String?, // TODO not null
        val lastAuthenticated: LocalDateTime? // TODO not null
)
