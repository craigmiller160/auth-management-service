package io.craigmiller160.authmanagementservice.dto

import java.time.LocalDateTime

data class ClientAuthDetailsDto (
        val lastAuthenticated: LocalDateTime
)
