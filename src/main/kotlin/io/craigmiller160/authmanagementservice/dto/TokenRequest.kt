package io.craigmiller160.authmanagementservice.dto

data class TokenRequest (
        val grant_type: String,
        val client_id: String? = null,
        val code: String? = null,
        val redirect_uri: String? = null,
        val refresh_token: String? = null
)
