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
    fun getMe(): MeResponse {
        val username = securityService.getCurrentUsername() ?: return MeResponse("", "")
        val userProfile = userService.findOrCreateUser(username)
        return MeResponse(userProfile.id.value.toString(), userProfile.name)
    }
}
