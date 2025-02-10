package de.neuefische.backend.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class SecurityService {
    fun getCurrentUsername(): String? {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication?.principal !is OAuth2User) return null

        val oAuth2User = authentication.principal as OAuth2User
        return oAuth2User.attributes["login"]?.toString()
    }
}
