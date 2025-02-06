package de.neuefische.backend.security

import de.neuefische.backend.user.UserService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService,
) {
    @GetMapping("/me")
    fun getMe(
        @AuthenticationPrincipal user: OAuth2User?,
    ): String {
        if (user == null) {
            return ""
        }

        val oauthUsername: String = user.attributes["login"]?.toString() ?: ""

        if (oauthUsername.isEmpty()) {
            return ""
        }

        return userService.findOrCreateUser(oauthUsername).name
    }
}
