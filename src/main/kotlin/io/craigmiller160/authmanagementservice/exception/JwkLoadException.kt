package io.craigmiller160.authmanagementservice.exception

import java.lang.RuntimeException

class JwkLoadException(msg: String, ex: Throwable) : RuntimeException(msg, ex)