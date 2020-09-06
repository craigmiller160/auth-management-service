package io.craigmiller160.authmanagementservice.entity

import javax.persistence.*

@Entity
@Table(name = "client_redirect_uris")
data class ClientRedirectUri (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,
        val clientId: Long,
        val redirectUri: String
)