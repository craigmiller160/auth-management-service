package io.craigmiller160.authmanagementservice.security

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.proc.JWSVerificationKeySelector
import com.nimbusds.jose.proc.SecurityContext
import com.nimbusds.jwt.proc.DefaultJWTProcessor
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtValidationFilter (
        private val jwkSet: JWKSet
) : OncePerRequestFilter() {

    // TODO make this more configurable to switch between header and cookie

    override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse, chain: FilterChain) {
        val token = getToken(req)

        val jwtProcessor = DefaultJWTProcessor<SecurityContext>()
        val keySource = ImmutableJWKSet<SecurityContext>(jwkSet)
        val expectedAlg = JWSAlgorithm.RS256
        val keySelector = JWSVerificationKeySelector(expectedAlg, keySource)
        jwtProcessor.jwsKeySelector = keySelector

        val claims = jwtProcessor.process(token, null)
        println("CLAIMS") // TODO delete this
        println(claims) // TODO delete this

        chain.doFilter(req, res)
    }

    private fun getToken(req: HttpServletRequest): String {
        val token = req.getHeader("Authorization")
        return token.replace("Bearer ", "")
    }


}