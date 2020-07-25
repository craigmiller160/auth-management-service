package io.craigmiller160.authmanagementservice.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Entity not found")
class EntityNotFoundException(msg: String = "") : RuntimeException(msg)
