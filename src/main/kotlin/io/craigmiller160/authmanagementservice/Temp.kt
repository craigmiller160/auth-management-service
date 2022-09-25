package io.craigmiller160.authmanagementservice

import org.springframework.security.crypto.bcrypt.BCrypt

fun main() {
    val result = BCrypt.hashpw("1566aadf-800f-4a9d-9828-6a77426a53b5", BCrypt.gensalt())
    println(result)
}