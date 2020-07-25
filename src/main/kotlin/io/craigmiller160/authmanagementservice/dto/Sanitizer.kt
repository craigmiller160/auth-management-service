package io.craigmiller160.authmanagementservice.dto

interface Sanitizer<T> {

    fun sanitize(): T

}
