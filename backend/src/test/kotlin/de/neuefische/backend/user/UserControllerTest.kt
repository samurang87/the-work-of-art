package de.neuefische.backend.user

import de.neuefische.backend.common.Medium
import org.bson.BsonObjectId
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class UserControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var userRepo: UserRepo

    @BeforeEach
    fun setUp() {
        userRepo.deleteAll()
    }

    @AfterEach
    fun tearDown() {
        userRepo.deleteAll()
    }

    @Test
    fun getUserByName() {
        // Given
        val user =
            User(
                id = BsonObjectId(),
                name = "test-user",
                bio = "test-bio",
                imageUrl = "test-image-url",
                mediums = listOf(Medium.WATERCOLORS, Medium.INK),
            )
        userRepo.save(user)

        // When
        val result = mockMvc.perform(get("/api/user/").param("name", "test-user"))

        // Then
        result
            .andExpect(status().isOk)
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
        val user =
            User(
                id = BsonObjectId(),
                name = "test-user",
            )
        userRepo.save(user)

        // When
        val result = mockMvc.perform(get("/api/user/").param("name", "test-user"))

        // Then
        result
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.name").value("test-user"))
            .andExpect(jsonPath("$.bio").value(null))
            .andExpect(jsonPath("$.imageUrl").value(null))
            .andExpect(jsonPath("$.mediums").isArray)
            .andExpect(jsonPath("$.mediums").isEmpty)
    }

    @Test
    fun getUserByNameReturnsNotFound() {
        // When
        val result = mockMvc.perform(get("/api/user/").param("name", "test-user"))

        // Then
        result.andExpect(status().isNotFound)
    }

    @Test
    fun getUserById() {
        // Given
        val userId = BsonObjectId()
        val user =
            User(
                id = userId,
                name = "test-user",
                bio = "test-bio",
                imageUrl = "test-image-url",
                mediums = listOf(Medium.WATERCOLORS, Medium.INK),
            )
        userRepo.save(user)

        // When
        val result = mockMvc.perform(get("/api/user/").param("id", userId.value.toString()))

        // Then
        result
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(userId.value.toString()))
            .andExpect(jsonPath("$.name").value("test-user"))
            .andExpect(jsonPath("$.bio").value("test-bio"))
            .andExpect(jsonPath("$.imageUrl").value("test-image-url"))
            .andExpect(jsonPath("$.mediums").isArray)
            .andExpect(jsonPath("$.mediums[0]").value("watercolors"))
            .andExpect(jsonPath("$.mediums[1]").value("ink"))
    }

    @Test
    fun getUserByIdWithoutOptionalFields() {
        // Given
        val userId = BsonObjectId()
        val user =
            User(
                id = userId,
                name = "test-user",
            )
        userRepo.save(user)

        // When
        val result = mockMvc.perform(get("/api/user/").param("id", userId.value.toString()))

        // Then
        result
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(userId.value.toString()))
            .andExpect(jsonPath("$.name").value("test-user"))
            .andExpect(jsonPath("$.bio").value(null))
            .andExpect(jsonPath("$.imageUrl").value(null))
            .andExpect(jsonPath("$.mediums").isArray)
            .andExpect(jsonPath("$.mediums").isEmpty)
    }

    @Test
    fun getUserByIdReturnsNotFound() {
        // Given
        val userId = BsonObjectId()

        // When
        val result = mockMvc.perform(get("/api/user/").param("id", userId.value.toString()))

        // Then
        result.andExpect(status().isNotFound)
    }

    @Test
    fun getUserWithoutParametersReturnsBadRequest() {
        // When
        val result = mockMvc.perform(get("/api/user/"))

        // Then
        result.andExpect(status().isBadRequest)
    }
}
