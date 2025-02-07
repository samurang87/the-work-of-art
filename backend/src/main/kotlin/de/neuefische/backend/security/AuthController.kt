package de.neuefische.backend.security

import de.neuefische.backend.user.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService,
    private val securityService: SecurityService,
) {
    @GetMapping("/me")
    fun getMe(): String {
        val username = securityService.getCurrentUsername() ?: return ""
        return userService.findOrCreateUser(username).name
    }
}
