package io.craigmiller160.authmanagementservice

import io.craigmiller160.authmanagementservice.client.AuthServerClient
import io.craigmiller160.authmanagementservice.security.AllowAllHostnameVerifier
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.security.KeyStore
import javax.annotation.PostConstruct
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

@SpringBootApplication
class AuthManagementServiceApplication

private const val TRUST_STORE_TYPE = "JKS"
private const val TRUST_STORE_PATH = "truststore.jks"
private const val TRUST_STORE_PASSWORD = "changeit"

fun main(args: Array<String>) {
	setupTls()
	runApplication<AuthManagementServiceApplication>(*args)
}

fun setupTls() {
	val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())

	val trustStore = KeyStore.getInstance(TRUST_STORE_TYPE)
	val trustStoreStream = AuthManagementServiceApplication::class.java.classLoader.getResourceAsStream(TRUST_STORE_PATH)
	trustStore.load(trustStoreStream, TRUST_STORE_PASSWORD.toCharArray())

	trustManagerFactory.init(trustStore)

	val trustManagers = trustManagerFactory.trustManagers
	val sslContext = SSLContext.getInstance("TLS")
	sslContext.init(null, trustManagers, null)
	SSLContext.setDefault(sslContext)

	HttpsURLConnection.setDefaultHostnameVerifier(AllowAllHostnameVerifier())
}
