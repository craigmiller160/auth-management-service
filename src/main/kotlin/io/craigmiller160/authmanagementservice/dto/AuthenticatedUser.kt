package io.craigmiller160.authmanagementservice.dto

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class AuthenticatedUser (
        username: String,
        authorities: List<GrantedAuthority>
): UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return this.authorities
    }

    override fun getUsername(): kotlin.String {
        return this.username
    }

    override fun isEnabled() = true
    override fun isCredentialsNonExpired() = true
    override fun getPassword() = ""
    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true

}

