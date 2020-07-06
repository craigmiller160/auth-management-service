package io.craigmiller160.authmanagementservice.controller

import io.craigmiller160.authmanagementservice.service.AuthCodeService
import org.springframework.http.ResponseCookie
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.math.BigInteger
import java.security.SecureRandom
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/authcode")
class AuthCodeController (
        private val authCodeService: AuthCodeService
) {

    companion object {
        private val STATE_ATTR = "state"
    }

    private fun generateAuthCodeState(): String { // TODO test this
        val random = SecureRandom()
        val bigInt = BigInteger(130, random)
        return bigInt.toString(32)
    }

    @GetMapping("/login")
    fun login(req: HttpServletRequest, res: HttpServletResponse) {
        val state = generateAuthCodeState()
        req.session.setAttribute(STATE_ATTR, state)
        val authCodeLoginUrl = authCodeService.getAuthCodeLoginUrl(state)
        res.status = 302
        res.addHeader("Location", authCodeLoginUrl)
    }

    @GetMapping("/code")
    fun code(@RequestParam("code") code: String, req: HttpServletRequest, res: HttpServletResponse) {
        println("STATE: ${req.session.getAttribute("state")}") // TODO delete this
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
