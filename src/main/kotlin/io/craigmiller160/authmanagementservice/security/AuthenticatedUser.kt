package io.craigmiller160.authmanagementservice.security

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class AuthenticatedUser (
        private val userName: String,
        private val grantedAuthorities: List<GrantedAuthority>
): UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return this.grantedAuthorities.toMutableList()
    }

    override fun getUsername(): String {
        return this.userName
    }

    @JsonIgnore
    override fun isEnabled() = true
    @JsonIgnore
    override fun isCredentialsNonExpired() = true
    @JsonIgnore
    override fun getPassword() = ""
    @JsonIgnore
    override fun isAccountNonExpired() = true
    @JsonIgnore
    override fun isAccountNonLocked() = true

}

