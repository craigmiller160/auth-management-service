package io.craigmiller160.authmanagementservice.testutils

import io.craigmiller160.authmanagementservice.entity.Client
import io.craigmiller160.authmanagementservice.entity.ClientUser
import io.craigmiller160.authmanagementservice.entity.Role
import io.craigmiller160.authmanagementservice.entity.User

object TestData {

    fun createUser(id: Long) = User(
            id,
            email = "craig_$id@gmail.com",
            firstName = "Craig_$id",
            lastName = "Miller_$id",
            password = "password",
            enabled = true
    )

    fun createRole(id: Long, clientId: Long) = Role(
            id,
            name = "Role_$id",
            clientId = clientId
    )

    fun createClientUser(clientId: Long, userId: Long) = ClientUser(
            id = 0,
            clientId = clientId,
            userId = userId
    )

    fun createClient(id: Long) = Client(
            id,
            name = "Client_$id",
            clientKey = "Key_$id",
            clientSecret = "Secret_$id",
            enabled = true,
            accessTokenTimeoutSecs = 0,
            refreshTokenTimeoutSecs = 0,
            authCodeTimeoutSecs = 0,
            clientRedirectUris = listOf()
    )

}
