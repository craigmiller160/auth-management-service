package io.craigmiller160.authmanagementservice.entity

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Suppress("JpaDataSourceORMInspection")
@Entity
@Table(name = "refresh_tokens")
data class RefreshToken (
        @Id
        val id: String,
        val refreshToken: String,
        val clientId: Long,
        val userId: Long?,
        val timestamp: LocalDateTime
)
