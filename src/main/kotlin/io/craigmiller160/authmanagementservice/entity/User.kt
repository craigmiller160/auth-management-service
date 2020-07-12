package io.craigmiller160.authmanagementservice.entity

import io.craigmiller160.authmanagementservice.dto.Sanitizer
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "users")
data class User (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,
        val email: String,
        val firstName: String,
        val lastName: String,
        val password: String
) : Sanitizer<User> {
        override fun sanitize(): User {
                return this.copy(password = "")
        }
}
