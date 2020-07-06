package io.craigmiller160.authmanagementservice.controller

import io.craigmiller160.authmanagementservice.security.AuthenticatedUser
import io.craigmiller160.authmanagementservice.dto.ClientList
import io.craigmiller160.authmanagementservice.dto.UserList
import io.craigmiller160.authmanagementservice.service.BasicService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/basic")
class BasicController (
        private val basicService: BasicService
) {

    @GetMapping("/users")
    fun getUsers(): UserList {
        return UserList(basicService.getUsers())
    }

    @GetMapping("/clients")
    fun getClients(): ClientList {
        return ClientList(basicService.getClients())
    }

    @GetMapping("/auth")
    fun getAuthenticatedUser(): AuthenticatedUser {
        return SecurityContextHolder.getContext().authentication.principal as AuthenticatedUser
    }

}
