package io.craigmiller160.authmanagementservice.controller

import io.craigmiller160.authmanagementservice.service.AuthCodeService
import org.springframework.http.ResponseCookie
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/authcode")
class AuthCodeController (
        private val authCodeService: AuthCodeService
) {

    @GetMapping("/login")
    fun login(res: HttpServletResponse) {
        val authCodeLoginUrl = authCodeService.getAuthCodeLoginUrl()
        res.status = 302
        res.addHeader("Location", authCodeLoginUrl)
    }

    @GetMapping("/code")
    fun code(@RequestParam("code") code: String, res: HttpServletResponse) {
        val (cookie, postAuthRedirect) = authCodeService.code(code)
        res.status = 302
        res.addHeader("Location", postAuthRedirect)
        res.addHeader("Set-Cookie", cookie.toString())
    }

    @GetMapping("/logout")
    fun logout(res: HttpServletResponse) {
        val cookie = authCodeService.logout()
        res.addHeader("Set-Cookie", cookie.toString())
    }

}
