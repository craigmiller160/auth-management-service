package io.craigmiller160.authmanagementservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AuthManagementServiceApplication

fun main(args: Array<String>) {
	runApplication<AuthManagementServiceApplication>(*args)
}
