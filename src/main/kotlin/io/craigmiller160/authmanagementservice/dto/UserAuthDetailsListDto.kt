package io.craigmiller160.authmanagementservice.dto

data class UserAuthDetailsListDto(
        val email: String,
        val authDetails: List<UserAuthDetailsDto>
)
