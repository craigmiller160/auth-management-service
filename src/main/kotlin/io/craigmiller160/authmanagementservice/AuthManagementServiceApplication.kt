package io.craigmiller160.authmanagementservice

import io.craigmiller160.authmanagementservice.client.AuthServerClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import javax.annotation.PostConstruct

@SpringBootApplication
class AuthManagementServiceApplication {

	// TODO delete this
	@Autowired
	private lateinit var authServerClient: AuthServerClient

	// TODO delete this
	@PostConstruct
	fun test() {
		authServerClient.getJwk()
	}

}

fun main(args: Array<String>) {
	runApplication<AuthManagementServiceApplication>(*args)
}
