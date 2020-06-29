package io.craigmiller160.authmanagementservice.dto

data class TokenResponse (
        val accessToken: String,
        val refreshToken: String
)
