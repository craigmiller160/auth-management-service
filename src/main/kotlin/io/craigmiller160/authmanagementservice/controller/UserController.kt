package io.craigmiller160.authmanagementservice.controller

import io.craigmiller160.authmanagementservice.olddto.FullUser
import io.craigmiller160.authmanagementservice.olddto.UserList
import io.craigmiller160.authmanagementservice.entity.User
import io.craigmiller160.authmanagementservice.service.LegacyUserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController (
        private val legacyUserService: LegacyUserService
) {

    // TODO unit tests

    @GetMapping
    fun getUsers(): ResponseEntity<UserList> {
        val users = legacyUserService.getUsers()
        if (users.users.isEmpty()) {
            return ResponseEntity.noContent().build()
        }
        return ResponseEntity.ok(users)
    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long): ResponseEntity<FullUser> {
        return legacyUserService.getUser(id)
                ?.let { ResponseEntity.ok(it) }
                ?: ResponseEntity.noContent().build()
    }

    @PostMapping
    fun createUser(@RequestBody user: User): FullUser {
        return legacyUserService.createUser(user)
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody user: FullUser): FullUser {
        return legacyUserService.updateUser(id, user)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): FullUser {
        return legacyUserService.deleteUser(id)
    }

}
