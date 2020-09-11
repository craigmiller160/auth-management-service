package io.craigmiller160.authmanagementservice.dto

import java.time.LocalDateTime

data class OldClientAuthDetailsDto (
        val tokenId: String?,
        val clientId: Long,
        val clientName: String,
        val lastAuthenticated: LocalDateTime?
)
