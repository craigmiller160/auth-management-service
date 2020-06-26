package io.craigmiller160.authmanagementservice.security

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthEntryPoint : AuthenticationEntryPoint {

    override fun commence(req: HttpServletRequest?, res: HttpServletResponse?, ex: AuthenticationException?) {
        ex?.printStackTrace() // TODO delete this
        TODO("Not yet implemented")
    }


}