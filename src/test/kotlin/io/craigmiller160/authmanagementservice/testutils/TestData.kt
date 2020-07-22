package io.craigmiller160.authmanagementservice.testutils

import io.craigmiller160.authmanagementservice.dto.ClientList
import io.craigmiller160.authmanagementservice.dto.UserList
import io.craigmiller160.authmanagementservice.entity.Client
import io.craigmiller160.authmanagementservice.entity.User

object TestData {

    fun createUser() = User(
            id = 0,
            email = "craig@gmail.com",
            firstName = "Craig",
            lastName = "Miller",
            password = "password"
    )

    fun createUserList() = UserList(listOf(createUser()))

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

    fun createClientList() = ClientList(listOf(createClient(1), createClient(2)))

}
