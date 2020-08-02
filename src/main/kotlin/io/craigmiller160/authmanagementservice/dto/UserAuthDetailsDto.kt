package io.craigmiller160.authmanagementservice.dto

import java.time.LocalDateTime

data class UserAuthDetailsDto (
        private val lastAuthenticated: LocalDateTime
)
