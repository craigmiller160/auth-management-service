/*
 *     Auth Management Service
 *     Copyright (C) 2020 Craig Miller
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.craigmiller160.authmanagementservice.dto

import io.craigmiller160.authmanagementservice.entity.Client
import io.craigmiller160.authmanagementservice.entity.ClientRedirectUri

data class ClientInputDto (
        val name: String,
        val clientKey: String,
        val clientSecret: String,
        val enabled: Boolean,
        val accessTokenTimeoutSecs: Int,
        val refreshTokenTimeoutSecs: Int,
        val authCodeTimeoutSecs: Int,
        val redirectUris: List<String>
) {

    fun getClientRedirectUris(clientId: Long = 0): List<ClientRedirectUri> =
            redirectUris.map { ClientRedirectUri(0, clientId, it) }

    fun toClient(clientId: Long = 0): Client {
        return Client(
                id = clientId,
                name = this.name,
                clientKey = this.clientKey,
                clientSecret = this.clientSecret,
                enabled = this.enabled,
                accessTokenTimeoutSecs = this.accessTokenTimeoutSecs,
                refreshTokenTimeoutSecs = this.refreshTokenTimeoutSecs,
                authCodeTimeoutSecs = this.authCodeTimeoutSecs
        )
    }
}
