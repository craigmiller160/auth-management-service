package io.craigmiller160.authmanagementservice.controller

import io.craigmiller160.authmanagementservice.service.AuthCodeService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/authcode")
class AuthCodeController (
        private val authCodeService: AuthCodeService
) {

    @GetMapping("/login")
    fun login() {
        TODO("Finish this")
    }

    @GetMapping("/code")
    fun code() {
        TODO("Finish this")
    }

}
