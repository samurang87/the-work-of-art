package de.neuefische.backend.security

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

    @Test
    @WithMockUser
    fun me_whenAuthenticated_returnsUserName() {
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
                                    "user",
                                )
                            },
                    ),
            ).andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string("user"))
    }

    @Test
    fun me_WhenNotAuthenticated_returnsEmptyUser() {
        mockMvc
            .perform(MockMvcRequestBuilders.get("/api/auth/me"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(""))
    }

    @Test
    @WithMockUser
    fun me_whenLoginNotFound_returnsEmptyUser() {
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
}
