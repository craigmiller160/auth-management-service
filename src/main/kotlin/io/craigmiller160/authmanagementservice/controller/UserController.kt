package io.craigmiller160.authmanagementservice.controller

import io.craigmiller160.authmanagementservice.dto.UserAuthDetailsListDto
import io.craigmiller160.authmanagementservice.service.UserAuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController (
        private val userAuthService: UserAuthService
) {

    @GetMapping("/auth/{userId}")
    fun getAllUserAuthDetails(@PathVariable userId: Long): UserAuthDetailsListDto {
        return userAuthService.getAllUserAuthDetails(userId)
    }

    @PostMapping("/auth/{userId}/{clientId}/revoke")
    fun revokeUserAuthAccess(@PathVariable clientId: Long, @PathVariable userId: Long): ResponseEntity<Void> {
        userAuthService.revokeUserAuthAccess(clientId, userId)
        return ResponseEntity.noContent().build()
    }

}
