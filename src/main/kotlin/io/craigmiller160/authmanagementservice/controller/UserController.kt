package io.craigmiller160.authmanagementservice.controller

import io.craigmiller160.authmanagementservice.dto.UserAuthDetailsDto
import io.craigmiller160.authmanagementservice.service.UserAuthService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController (
        private val userAuthService: UserAuthService
) {

    @GetMapping("/auth/{userId}/{clientId}")
    fun getUserAuthDetails(@PathVariable clientId: Long, @PathVariable userId: Long): UserAuthDetailsDto {
        return userAuthService.getUserAuthDetails(clientId, userId)
    }

    @GetMapping("/auth/{userId}")
    fun getAllUserAuthDetails(@PathVariable userId: Long): List<UserAuthDetailsDto> {
        return userAuthService.getAllUserAuthDetails(userId)
    }

    @PostMapping("/auth/{userId}/{clientId}/revoke")
    fun revokeUserAuthAccess(@PathVariable clientId: Long, @PathVariable userId: Long): UserAuthDetailsDto {
        return userAuthService.revokeUserAuthAccess(clientId, userId)
    }

}
