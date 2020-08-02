package io.craigmiller160.authmanagementservice.dto

import java.time.LocalDateTime

data class UserAuthDetailsDto (
        val tokenId: String?,
        val clientId: Long,
        val userId: Long,
        val lastAuthenticated: LocalDateTime?
)
