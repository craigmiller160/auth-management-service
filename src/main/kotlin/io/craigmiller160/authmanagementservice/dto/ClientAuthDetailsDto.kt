package io.craigmiller160.authmanagementservice.dto

data class ClientAuthDetailsDto(
        val clientName: String,
        val userAuthDetails: List<UserAuthDetailsDto>
)