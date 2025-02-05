package de.neuefische.backend.user

import de.neuefische.backend.common.Medium
import io.mockk.every
import io.mockk.mockk
import org.bson.BsonObjectId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull

class UserServiceTest {
    private val userRepo = mockk<UserRepo>()
    private val userService = UserService(userRepo)

    @Test
    fun getUserByName() {
        // Given
        val user =
            User(
                id = BsonObjectId(),
                name = "test-user",
                bio = "test-bio",
                imageUrl = "test-image-url",
                mediums =
                    listOf(
                        Medium.WATERCOLORS,
                        Medium.INK,
                    ),
            )

        val expectedResponse =
            UserProfileResponse(
                id = user.id.value.toString(),
                name = "test-user",
                bio = "test-bio",
                imageUrl = "test-image-url",
                mediums = listOf("watercolors", "ink"),
            )

        every { userRepo.findByName("test-user") } returns user

        // When
        val result = userService.getUserByName("test-user")

        // Then
        assertEquals(expectedResponse, result)
    }

    // add test for nonexistent name
    @Test
    fun getUserByNameNonExistent() {
        // Given
        every { userRepo.findByName("test-user") } returns null

        // When
        val result: UserProfileResponse? = userService.getUserByName("test-user")

        // Then
        assertEquals(null, result)
    }

    @Test
    fun getUserById() {
        // Given
        val user =
            User(
                id = BsonObjectId(),
                name = "test-user",
                bio = "test-bio",
                imageUrl = "test-image-url",
                mediums =
                    listOf(
                        Medium.WATERCOLORS,
                        Medium.INK,
                    ),
            )

        val expectedResponse =
            UserProfileResponse(
                id = user.id.value.toString(),
                name = "test-user",
                bio = "test-bio",
                imageUrl = "test-image-url",
                mediums = listOf("watercolors", "ink"),
            )

        every { userRepo.findByIdOrNull(user.id.value.toString()) } returns user

        // When
        val result = userService.getUserById(user.id.value.toString())

        // Then
        assertEquals(expectedResponse, result)
    }

    // add test for nonexistent id
    @Test
    fun getUserByIdNonExistent() {
        // Given
        every { userRepo.findByIdOrNull("test-id") } returns null

        // When
        val result: UserProfileResponse? = userService.getUserById("test-id")

        // Then
        assertEquals(null, result)
    }
}
