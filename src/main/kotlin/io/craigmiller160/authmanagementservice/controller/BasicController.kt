package io.craigmiller160.authmanagementservice.controller

import io.craigmiller160.authmanagementservice.entity.Client
import io.craigmiller160.authmanagementservice.entity.User
import io.craigmiller160.authmanagementservice.service.BasicService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/basic")
class BasicController (
        private val basicService: BasicService
) {

    @GetMapping("/users")
    fun getUsers(): List<User> {
        return basicService.getUsers()
    }

    @GetMapping("/clients")
    fun getClients(): List<Client> {
        return basicService.getClients()
    }

}