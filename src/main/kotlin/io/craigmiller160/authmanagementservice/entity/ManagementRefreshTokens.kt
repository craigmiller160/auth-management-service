package io.craigmiller160.authmanagementservice.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "management_refresh_tokens")
data class ManagementRefreshTokens (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,
        val tokenId: String,
        val refreshToken: String
)
