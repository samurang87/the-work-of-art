package de.neuefische.backend.security

import com.ninjasquad.springmockk.MockkBean
import de.neuefische.backend.common.Medium
import de.neuefische.backend.user.User
import de.neuefische.backend.user.UserRepo
import io.mockk.every
import io.mockk.verify
import org.bson.BsonObjectId
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var userRepo: UserRepo

    @Test
    @WithMockUser
    fun `should return user name if logged in user exists`() {
        val user =
            User(
                id = BsonObjectId(),
                name = "existing-user",
                bio = "test-bio",
                imageUrl = "test-image-url",
                mediums =
                    listOf(
                        Medium.WATERCOLORS,
                        Medium.INK,
                    ),
            )

        every { userRepo.findByName("existing-user") } returns user

        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get("/api/auth/me")
                    .with(
                        SecurityMockMvcRequestPostProcessors
                            .oidcLogin()
                            .userInfoToken { token: OidcUserInfo.Builder ->
                                token.claim(
                                    "login",
                                    "existing-user",
                                )
                            },
                    ),
            ).andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string("existing-user"))
        verify(exactly = 0) { userRepo.save(any()) }
    }

    @Test
    fun `should return empty string when not authenticated`() {
        mockMvc
            .perform(MockMvcRequestBuilders.get("/api/auth/me"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(""))
    }

    @Test
    @WithMockUser
    fun `should return empty string when login not found`() {
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get("/api/auth/me")
                    .with(
                        SecurityMockMvcRequestPostProcessors
                            .oidcLogin()
                            .userInfoToken { token: OidcUserInfo.Builder ->
                                token.claim(
                                    "login",
                                    null,
                                )
                            },
                    ),
            ).andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(""))
    }

    @Test
    @WithMockUser
    fun `should create user if logged in user not found`() {
        every { userRepo.findByName("new-user") } returns null
        every { userRepo.save(any()) } answers { firstArg() }

        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .get("/api/auth/me")
                    .with(
                        SecurityMockMvcRequestPostProcessors
                            .oidcLogin()
                            .userInfoToken { token: OidcUserInfo.Builder ->
                                token.claim(
                                    "login",
                                    "new-user",
                                )
                            },
                    ),
            ).andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string("new-user"))
        verify { userRepo.save(any()) }
    }
}
