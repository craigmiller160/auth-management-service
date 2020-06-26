package io.craigmiller160.authmanagementservice.security

import com.nimbusds.jose.JOSEException
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.proc.BadJOSEException
import com.nimbusds.jose.proc.JWSVerificationKeySelector
import com.nimbusds.jose.proc.SecurityContext
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.proc.DefaultJWTProcessor
import io.craigmiller160.authmanagementservice.exception.InvalidTokenException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.filter.OncePerRequestFilter
import java.lang.RuntimeException
import java.text.ParseException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtValidationFilter (
        private val jwkSet: JWKSet
) : OncePerRequestFilter() {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    // TODO make this more configurable to switch between header and cookie
    // TODO need to validate that the token is for this app

    override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse, chain: FilterChain) {
        try {
            val token = getToken(req)
            val claims = validateToken(token)
            SecurityContextHolder.getContext().authentication = createAuthentication(claims)
        } catch (ex: InvalidTokenException) {
            log.error("Error authenticating token", ex)
            SecurityContextHolder.clearContext()
        }

        chain.doFilter(req, res)
    }

    private fun validateToken(token: String): JWTClaimsSet {
        val jwtProcessor = DefaultJWTProcessor<SecurityContext>()
        val keySource = ImmutableJWKSet<SecurityContext>(jwkSet)
        val expectedAlg = JWSAlgorithm.RS256
        val keySelector = JWSVerificationKeySelector(expectedAlg, keySource)
        jwtProcessor.jwsKeySelector = keySelector

        try {
            return jwtProcessor.process(token, null)
        } catch (ex: Exception) {
            when(ex) {
                is ParseException, is JOSEException, is BadJOSEException ->
                    throw InvalidTokenException("Token validation failed", ex)
                is RuntimeException -> throw ex
                else -> throw RuntimeException(ex)
            }
        }
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
        val token = req.getHeader("Authorization") ?: throw InvalidTokenException("No token present")
        return token.replace("Bearer ", "")
    }


}