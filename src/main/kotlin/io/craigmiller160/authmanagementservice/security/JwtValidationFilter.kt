package io.craigmiller160.authmanagementservice.security

import com.nimbusds.jose.JOSEException
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.proc.BadJOSEException
import com.nimbusds.jose.proc.JWSVerificationKeySelector
import com.nimbusds.jose.proc.SecurityContext
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier
import com.nimbusds.jwt.proc.DefaultJWTProcessor
import io.craigmiller160.authmanagementservice.entity.ManagementRefreshToken
import io.craigmiller160.authmanagementservice.exception.InvalidTokenException
import io.craigmiller160.authmanagementservice.repository.ManagementRefreshTokenRepository
import io.craigmiller160.oauth2.client.AuthServerClient
import io.craigmiller160.oauth2.config.OAuthConfig
import io.craigmiller160.oauth2.dto.TokenResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.text.ParseException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtValidationFilter (
        private val oAuthConfig: OAuthConfig,
        private val manageRefreshTokenRepo: ManagementRefreshTokenRepository,
        private val authServerClient: AuthServerClient
) : OncePerRequestFilter() {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse, chain: FilterChain) {
        if (!req.requestURI.startsWith("/authcode")) {
            try {
                val token = getToken(req)
                val claims = validateToken(token)
                SecurityContextHolder.getContext().authentication = createAuthentication(claims)
            } catch (ex: InvalidTokenException) {
                log.error("Error authenticating token", ex)
                SecurityContextHolder.clearContext()
            }
        }

        chain.doFilter(req, res)
    }

    private fun validateToken(token: String, alreadyAttemptedRefresh: Boolean = false): JWTClaimsSet {
        val jwtProcessor = DefaultJWTProcessor<SecurityContext>()
        val keySource = ImmutableJWKSet<SecurityContext>(oAuthConfig.jwkSet)
        val expectedAlg = JWSAlgorithm.RS256
        val keySelector = JWSVerificationKeySelector(expectedAlg, keySource)
        jwtProcessor.jwsKeySelector = keySelector

        val claimsVerifier = DefaultJWTClaimsVerifier<SecurityContext>(
                JWTClaimsSet.Builder()
                        .claim("clientKey", oAuthConfig.clientKey)
                        .claim("clientName", oAuthConfig.clientName)
                        .build(),
                setOf("sub", "exp", "iat", "jti")
        )
        jwtProcessor.jwtClaimsSetVerifier = claimsVerifier

        try {
            return jwtProcessor.process(token, null)
        } catch (ex: Exception) {
            when(ex) {
                is BadJOSEException -> {
                    if (alreadyAttemptedRefresh) {
                        throw InvalidTokenException("Token validation failed", ex)
                    }

                    return attemptTokenRefresh(token)
                            ?.let { tokenResponse ->
                                validateToken(tokenResponse.accessToken, true)
                            }
                            ?: throw InvalidTokenException("Token validation failed", ex)
                }
                is ParseException, is JOSEException ->
                    throw InvalidTokenException("Token validation failed", ex)
                is RuntimeException -> throw ex
                else -> throw RuntimeException(ex)
            }
        }
    }

    private fun attemptTokenRefresh(token: String): TokenResponse? {
        val jwt = SignedJWT.parse(token)
        val claims = jwt.jwtClaimsSet
        return manageRefreshTokenRepo.findByTokenId(claims.jwtid)
                ?.let { refreshToken ->
                    try {
                        val tokenResponse = authServerClient.authenticateRefreshToken(refreshToken.refreshToken)
                        manageRefreshTokenRepo.deleteById(refreshToken.id)
                        manageRefreshTokenRepo.save(ManagementRefreshToken(0, tokenResponse.tokenId, tokenResponse.refreshToken))
                        tokenResponse
                    } catch (ex: Exception) {
                        log.error("Error refreshing token", ex)
                        null
                    }
                }
    }

    private fun createAuthentication(claims: JWTClaimsSet): Authentication {
        val authorities = claims.getStringListClaim("roles")
                .map { SimpleGrantedAuthority(it) }
        val authUser = AuthenticatedUser(
                userName = claims.subject,
                grantedAuthorities = authorities,
                firstName = claims.getStringClaim("firstName"),
                lastName = claims.getStringClaim("lastName"),
                tokenId = claims.jwtid
        )
        return UsernamePasswordAuthenticationToken(authUser, "", authUser.authorities)
    }

    private fun getToken(req: HttpServletRequest): String {
        var token: String? = null
        if (oAuthConfig.acceptBearerToken) {
            token = getBearerToken(req)
        }

        if (token == null && oAuthConfig.acceptCookie) {
            token = getCookieToken(req)
        }

        if (token == null) {
            throw InvalidTokenException("Token not found")
        }

        return token
    }

    private fun getCookieToken(req: HttpServletRequest): String? {
        return req.cookies?.find { cookie -> cookie.name == oAuthConfig.cookieName }?.value
    }

    private fun getBearerToken(req: HttpServletRequest): String? {
        val token = req.getHeader("Authorization")
        return token?.let {
            if (!it.startsWith("Bearer ")) {
                throw InvalidTokenException("Not bearer token")
            }
            it.replace("Bearer ", "")
        }

    }


}
