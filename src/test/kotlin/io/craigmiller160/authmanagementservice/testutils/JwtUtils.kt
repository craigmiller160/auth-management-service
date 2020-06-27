package io.craigmiller160.authmanagementservice.testutils

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jose.crypto.impl.RSASSA
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.KeyUse
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.interfaces.RSAPublicKey

object JwtUtils {

    fun createKeyPair(): KeyPair {
        val keyPairGen = KeyPairGenerator.getInstance("RSA")
        return keyPairGen.genKeyPair()
    }

    fun createJwkSet(keyPair: KeyPair): JWKSet {
        val builder = RSAKey.Builder(keyPair.public as RSAPublicKey)
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.RS256)
                .keyID("oauth-jwt")
        return JWKSet(builder.build())
    }

    fun createJwt(): SignedJWT {
        val header = JWSHeader.Builder(JWSAlgorithm.RS256)
                .build()

        val claims = JWTClaimsSet.Builder()
                .subject("username")
                .claim("roles", listOf<String>())
                .build()
        return SignedJWT(header, claims)
    }

    fun signAndSerializeJwt(jwt: SignedJWT, privateKey: PrivateKey): String {
        val signer = RSASSASigner(privateKey)
        jwt.sign(signer)
        return jwt.serialize()
    }

}
