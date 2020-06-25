package io.craigmiller160.authmanagementservice.security

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.proc.JWSVerificationKeySelector
import com.nimbusds.jose.proc.SecurityContext
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.proc.DefaultJWTProcessor
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtValidationFilter (
        private val jwkSet: JWKSet
) : OncePerRequestFilter() {

    // TODO make this more configurable to switch between header and cookie
    // TODO need to validate that the token is for this app

    override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse, chain: FilterChain) {
        val token = getToken(req)
        val claims = validateToken(token)
        SecurityContextHolder.getContext().authentication = createAuthentication(claims)
        chain.doFilter(req, res)
    }

    private fun validateToken(token: String): JWTClaimsSet {
        val jwtProcessor = DefaultJWTProcessor<SecurityContext>()
        val keySource = ImmutableJWKSet<SecurityContext>(jwkSet)
        val expectedAlg = JWSAlgorithm.RS256
        val keySelector = JWSVerificationKeySelector(expectedAlg, keySource)
        jwtProcessor.jwsKeySelector = keySelector

        return jwtProcessor.process(token, null)
    }

    private fun createAuthentication(claims: JWTClaimsSet): Authentication {
        val authorities = claims.getStringListClaim("roles")
                .map { SimpleGrantedAuthority(it) }
        val userDetails = User.withUsername(claims.subject)
                .password("")
                .authorities(authorities)
                .build()
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    private fun getToken(req: HttpServletRequest): String {
        val token = req.getHeader("Authorization")
        return token.replace("Bearer ", "")
    }


}