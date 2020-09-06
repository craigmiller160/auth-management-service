package io.craigmiller160.authmanagementservice.entity

import io.craigmiller160.authmanagementservice.dto.Sanitizer
import javax.persistence.*

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
        val authCodeTimeoutSecs: Int,

        @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true)
        @JoinColumn(name = "clientId", insertable = false, updatable = false)
        val clientRedirectUris: List<ClientRedirectUri>
) : Sanitizer<Client> {

        fun getRedirectUris(): List<String> {
                return clientRedirectUris.map { it.redirectUri }
        }

        override fun sanitize(): Client {
                return this.copy(clientSecret = "")
        }
}
