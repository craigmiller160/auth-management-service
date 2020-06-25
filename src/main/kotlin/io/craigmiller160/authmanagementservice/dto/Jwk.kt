package io.craigmiller160.authmanagementservice.dto

data class Jwk (
        val kty: String,
        val e: String,
        val use: String,
        val kid: String,
        val alg: String,
        val n: String
)