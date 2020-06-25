package io.craigmiller160.authmanagementservice.client

import io.craigmiller160.authmanagementservice.dto.JwkList
import org.springframework.web.client.RestTemplate

class AuthServerClient (
        private val restTemplate: RestTemplate
) {

    // TODO make this configurable

    fun getJwk() {
        val jwkList = restTemplate.getForEntity("https://localhost:7003/jwk", JwkList::class.java)
        println("JWK List") // TODO delete this
        println(jwkList) // TODO delete this
    }

}