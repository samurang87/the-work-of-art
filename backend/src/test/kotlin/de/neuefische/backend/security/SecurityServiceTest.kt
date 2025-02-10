package de.neuefische.backend.security

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.core.user.OAuth2User
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SecurityServiceTest {
    private lateinit var securityService: SecurityService

    @BeforeEach
    fun setUp() {
        securityService = SecurityService()
        mockkStatic(SecurityContextHolder::class)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getCurrentUsername should return username when authenticated`() {
        val authentication = mockk<Authentication>()
        val securityContext = mockk<SecurityContext>()
        val oAuth2User = mockk<OAuth2User>()

        every { SecurityContextHolder.getContext() } returns securityContext
        every { securityContext.authentication } returns authentication
        every { authentication.principal } returns oAuth2User
        every { oAuth2User.attributes } returns mapOf("login" to "test-user")

        val result = securityService.getCurrentUsername()

        assertEquals("test-user", result)
    }

    @Test
    fun `getCurrentUsername should return null when not authenticated`() {
        val securityContext = mockk<SecurityContext>()

        every { SecurityContextHolder.getContext() } returns securityContext
        every { securityContext.authentication } returns null

        val result = securityService.getCurrentUsername()

        assertNull(result)
    }

    @Test
    fun `getCurrentUsername should return null when principal is not OAuth2User`() {
        val authentication = mockk<Authentication>()
        val securityContext = mockk<SecurityContext>()

        every { SecurityContextHolder.getContext() } returns securityContext
        every { securityContext.authentication } returns authentication
        every { authentication.principal } returns "not-an-oauth2-user"

        val result = securityService.getCurrentUsername()

        assertNull(result)
    }
}
