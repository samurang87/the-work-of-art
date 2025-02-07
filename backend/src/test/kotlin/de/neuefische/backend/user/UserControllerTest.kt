package de.neuefische.backend.user

import com.fasterxml.jackson.databind.ObjectMapper
import de.neuefische.backend.common.Medium
import org.bson.BsonObjectId
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
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

    // --- GET /api/user/ ---

    @Test
    fun `should return user by name`() {
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
        val result =
            mockMvc.perform(
                get("/api/user/").with(oauth2Login()).param("name", "test-user"),
            )

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
    fun `should return user by name without optional fields`() {
        // Given
        val user =
            User(
                id = BsonObjectId(),
                name = "test-user",
            )
        userRepo.save(user)

        // When
        val result =
            mockMvc.perform(
                get("/api/user/").with(oauth2Login()).param("name", "test-user"),
            )

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
    fun `get should return not found when user by name does not exist`() {
        // When
        val result =
            mockMvc.perform(
                get("/api/user/").with(oauth2Login()).param("name", "test-user"),
            )

        // Then
        result.andExpect(status().isNotFound)
    }

    @Test
    fun `should return user by id`() {
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
        val result =
            mockMvc.perform(
                get("/api/user/")
                    .with(oauth2Login())
                    .param("id", userId.value.toString()),
            )

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
    fun `should return user by id without optional fields`() {
        // Given
        val userId = BsonObjectId()
        val user =
            User(
                id = userId,
                name = "test-user",
            )
        userRepo.save(user)

        // When
        val result =
            mockMvc.perform(
                get("/api/user/")
                    .with(oauth2Login())
                    .param("id", userId.value.toString()),
            )

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
    fun `should return not found when user by id does not exist`() {
        // Given
        val userId = BsonObjectId()

        // When
        val result =
            mockMvc.perform(
                get("/api/user/")
                    .with(oauth2Login())
                    .param("id", userId.value.toString()),
            )

        // Then
        result.andExpect(status().isNotFound)
    }

    @Test
    fun `should return bad request when no parameters are provided`() {
        // When
        val result = mockMvc.perform(get("/api/user/").with(oauth2Login()))

        // Then
        result.andExpect(status().isBadRequest)
    }

    // --- PUT /api/user/{id} ---

    @Test
    fun `should update user by id`() {
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

        val req =
            UserProfileUpdateRequest(
                bio = "updated-bio",
                imageUrl = "updated-image-url",
                mediums = listOf("acrylic", "oil"),
            )

        // When
        val result =
            mockMvc.perform(
                put("/api/user/{id}", userId.value.toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        ObjectMapper().writeValueAsString(req),
                    ).with(
                        oauth2Login().attributes { attrs ->
                            attrs["login"] = "test-user"
                        },
                    ),
            )

        // Then
        result
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(userId.value.toString()))
            .andExpect(jsonPath("$.name").value("test-user"))
            .andExpect(jsonPath("$.bio").value("updated-bio"))
            .andExpect(jsonPath("$.imageUrl").value("updated-image-url"))
            .andExpect(jsonPath("$.mediums").isArray)
            .andExpect(jsonPath("$.mediums[0]").value("acrylic"))
            .andExpect(jsonPath("$.mediums[1]").value("oil"))
    }

    @Test
    fun `put should return not found when user by id does not exist`() {
        // Given
        val userId = BsonObjectId()
        val req =
            UserProfileUpdateRequest(
                bio = "updated-bio",
                imageUrl = "updated-image-url",
                mediums = listOf("acrylics", "oil"),
            )

        // When
        val result =
            mockMvc.perform(
                put("/api/user/{id}", userId.value.toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        ObjectMapper().writeValueAsString(req),
                    ).with(
                        oauth2Login().attributes { attrs ->
                            attrs["login"] = "test-user"
                        },
                    ),
            )

        // Then
        result.andExpect(status().isNotFound)
    }

    @Test
    fun `put should return unauthorized when user is not authenticated`() {
        // Given
        val userId = BsonObjectId()
        val req =
            UserProfileUpdateRequest(
                bio = "updated-bio",
                imageUrl = "updated-image-url",
                mediums = listOf("acrylics", "oil"),
            )

        // When
        val result =
            mockMvc.perform(
                put("/api/user/{id}", userId.value.toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        ObjectMapper().writeValueAsString(req),
                    ),
            )

        // Then
        result.andExpect(status().isUnauthorized)
    }

    @Test
    fun `put should return forbidden when user is not authorized`() {
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
        val req =
            UserProfileUpdateRequest(
                bio = "updated-bio",
                imageUrl = "updated-image-url",
                mediums = listOf("acrylics", "oil"),
            )

        // When
        val result =
            mockMvc.perform(
                put("/api/user/{id}", userId.value.toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        ObjectMapper().writeValueAsString(req),
                    ).with(
                        oauth2Login().attributes { attrs ->
                            attrs["login"] = "another-user"
                        },
                    ),
            )

        // Then
        result.andExpect(status().isForbidden)
    }
}
