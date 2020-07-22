package io.craigmiller160.authmanagementservice.testutils

import io.craigmiller160.authmanagementservice.dto.ClientList
import io.craigmiller160.authmanagementservice.dto.UserList
import io.craigmiller160.authmanagementservice.entity.Client
import io.craigmiller160.authmanagementservice.entity.ClientUser
import io.craigmiller160.authmanagementservice.entity.Role
import io.craigmiller160.authmanagementservice.entity.User

object TestData {

    fun createUser(id: Long) = User(
            id = id,
            email = "craig_$id@gmail.com",
            firstName = "Craig_$id",
            lastName = "Miller_$id",
            password = "password"
    )

    fun createRole(id: Long, clientId: Long) = Role(
            id = id,
            name = "Role_$id",
            clientId = clientId
    )

    fun createClientUser(clientId: Long, userId: Long) = ClientUser(
            id = 0,
            clientId = clientId,
            userId = userId
    )

    fun createClient(id: Long) = Client(
            id = id,
            name = "Client_$id",
            clientKey = "Key_$id",
            clientSecret = "Secret_$id",
            enabled = true,
            allowClientCredentials = false,
            allowPassword = false,
            allowAuthCode = false,
            accessTokenTimeoutSecs = 0,
            refreshTokenTimeoutSecs = 0
    )

}
