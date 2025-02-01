package de.neuefische.backend.user

import com.ninjasquad.springmockk.MockkBean
import de.neuefische.backend.common.Medium
import io.mockk.every
import org.bson.BsonObjectId
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var userRepo: UserRepo

    @Test
    fun getUserByName() {
        // Given
        val user = User(
            id = BsonObjectId(),
            name = "test-user",
            bio = "test-bio",
            imageUrl = "test-image-url",
            mediums = listOf(
                Medium.WATERCOLORS, Medium.INK,
            )
        )

        every { userRepo.findByName("test-user") } returns user

        // When
        val result = mockMvc.perform(get("/api/user/test-user"))

        // Then
        result.andExpect(status().isOk)
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.name").value("test-user"))
            .andExpect(jsonPath("$.bio").value("test-bio"))
            .andExpect(jsonPath("$.imageUrl").value("test-image-url"))
            .andExpect(jsonPath("$.mediums").isArray)
            .andExpect(jsonPath("$.mediums[0]").value("watercolors"))
            .andExpect(jsonPath("$.mediums[1]").value("ink"))
    }

    @Test
    fun getUserByNameWithoutOptionalFields() {
        // Given
        val user = User(
            id = BsonObjectId(),
            name = "test-user",
        )

        every { userRepo.findByName("test-user") } returns user

        // When
        val result = mockMvc.perform(get("/api/user/test-user"))

        // Then
        result.andExpect(status().isOk)
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.name").value("test-user"))
            .andExpect(jsonPath("$.bio").value(null))
            .andExpect(jsonPath("$.imageUrl").value(null))
            .andExpect(jsonPath("$.mediums").isArray)
            .andExpect(jsonPath("$.mediums").isEmpty)
    }

    @Test
    fun getUserByNameReturnsNotFound() {
        // Given
        every { userRepo.findByName("test-user") } returns null

        // When
        val result = mockMvc.perform(get("/api/user/test-user"))

        // Then
        result.andExpect(status().isNotFound)
    }
}
