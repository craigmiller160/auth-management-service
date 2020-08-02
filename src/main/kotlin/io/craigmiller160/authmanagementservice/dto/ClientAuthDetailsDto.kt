package io.craigmiller160.authmanagementservice.dto

import java.time.LocalDateTime

data class ClientAuthDetailsDto (
        val tokenId: String?,
        val clientId: Long,
        val lastAuthenticated: LocalDateTime?
)
