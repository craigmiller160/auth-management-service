package io.craigmiller160.authmanagementservice.dto

import java.time.LocalDateTime

data class UserAuthDetailsDto (
        val tokenId: String,
        val clientId: Long,
        val clientName: String,
        val userId: Long,
        val userEmail: String?, // TODO not null
        val lastAuthenticated: LocalDateTime
)
