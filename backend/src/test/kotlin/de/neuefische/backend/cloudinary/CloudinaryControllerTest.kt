package de.neuefische.backend.cloudinary

import com.cloudinary.Cloudinary
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(CloudinaryController::class)
@WithMockUser
class CloudinaryControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var cloudinary: Cloudinary

    @BeforeEach
    fun setUp() {
        every { cloudinary.config.apiSecret } returns "test-secret"
        every { cloudinary.config.apiKey } returns "test-key"
        every { cloudinary.config.cloudName } returns "test-cloud"
    }

    @Test
    fun `GET signature should return 200 and valid signature response`() {
        mockMvc
            .perform(get("/api/cloudinary/signature"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.apiKey").value("test-key"))
            .andExpect(jsonPath("$.cloudName").value("test-cloud"))
            .andExpect(jsonPath("$.uploadPreset").value("woa-standard"))
            .andExpect(jsonPath("$.timestamp").exists())
            .andExpect(jsonPath("$.signature").exists())
    }

    @Test
    fun `GET signature should return 400 when apiSecret is missing`() {
        every { cloudinary.config.apiSecret } returns null

        mockMvc
            .perform(get("/api/cloudinary/signature"))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `GET signature should return 400 when apiKey is missing`() {
        every { cloudinary.config.apiKey } returns null

        mockMvc
            .perform(get("/api/cloudinary/signature"))
            .andExpect(status().isBadRequest)
    }
}
