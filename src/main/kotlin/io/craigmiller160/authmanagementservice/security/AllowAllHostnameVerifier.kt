package io.craigmiller160.authmanagementservice.security

import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession

class AllowAllHostnameVerifier : HostnameVerifier {
    override fun verify(hostname: String?, session: SSLSession?): Boolean = true
}