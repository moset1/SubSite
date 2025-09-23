package com.semocompany.backend.domain.user.dto

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User

class CustomOAuth2User(
    private val attributes: MutableMap<String?, Any?>?,
    private val authorities: MutableCollection<out GrantedAuthority?>?,
    private val username: String?
) : OAuth2User {
    override fun getAttributes(): MutableMap<String?, Any?>? {
        return attributes
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority?>? {
        return authorities
    }

    override fun getName(): String? {
        return username
    }
}
