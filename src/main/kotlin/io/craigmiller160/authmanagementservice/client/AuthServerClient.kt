package io.craigmiller160.authmanagementservice.client

import io.craigmiller160.authmanagementservice.dto.TokenResponse

interface AuthServerClient {
    fun authCodeLogin(code: String): TokenResponse
    fun tokenRefresh(refreshToken: String): TokenResponse
}
