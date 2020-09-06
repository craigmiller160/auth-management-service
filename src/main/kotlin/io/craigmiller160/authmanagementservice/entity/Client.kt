package io.craigmiller160.authmanagementservice.entity

import io.craigmiller160.authmanagementservice.dto.Sanitizer
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "clients")
data class Client (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,
        val name: String,
        val clientKey: String,
        val clientSecret: String,
        val enabled: Boolean,
        val accessTokenTimeoutSecs: Int,
        val refreshTokenTimeoutSecs: Int,
        val authCodeTimeoutSecs: Int
) : Sanitizer<Client> {
        override fun sanitize(): Client {
                return this.copy(clientSecret = "")
        }
}
