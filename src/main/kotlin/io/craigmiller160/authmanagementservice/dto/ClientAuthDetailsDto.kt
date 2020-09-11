package io.craigmiller160.authmanagementservice.dto

data class ClientAuthDetailsDto(
        private val clientName: String,
        private val userAuthDetails: List<UserAuthDetailsDto>
)