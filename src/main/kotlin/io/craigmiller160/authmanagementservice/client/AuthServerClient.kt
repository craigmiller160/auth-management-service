package io.craigmiller160.authmanagementservice.client

import io.craigmiller160.authmanagementservice.dto.TokenResponse

interface AuthServerClient {
    fun authenticateAuthCode(code: String): TokenResponse
    fun authenticateRefreshToken(refreshToken: String): TokenResponse
}
