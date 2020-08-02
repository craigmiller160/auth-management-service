package io.craigmiller160.authmanagementservice.dto

import java.time.LocalDateTime

data class UserAuthDetailsDto (
        private val tokenId: String?,
        private val clientId: Long,
        private val userId: Long,
        private val lastAuthenticated: LocalDateTime?
)
