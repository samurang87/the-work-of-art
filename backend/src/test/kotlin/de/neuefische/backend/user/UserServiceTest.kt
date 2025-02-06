package de.neuefische.backend.user

import de.neuefische.backend.common.Medium
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.bson.BsonObjectId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull

class UserServiceTest {
    private val userRepo = mockk<UserRepo>()
    private val userService = UserService(userRepo)

    @Test
    fun `should return user profile when user exists by name`() {
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
        verify { userRepo.findByName("test-user") }
    }

    // add test for nonexistent name
    @Test
    fun `should return null when user does not exist by name`() {
        // Given
        every { userRepo.findByName("test-user") } returns null

        // When
        val result: UserProfileResponse? = userService.getUserByName("test-user")

        // Then
        assertEquals(null, result)
        verify { userRepo.findByName("test-user") }
    }

    @Test
    fun `should return user profile when user exists by id`() {
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
        verify { userRepo.findByIdOrNull(user.id.value.toString()) }
    }

    @Test
    fun `should return null when user does not exist by id`() {
        // Given
        every { userRepo.findByIdOrNull("test-id") } returns null

        // When
        val result: UserProfileResponse? = userService.getUserById("test-id")

        // Then
        assertEquals(null, result)
        verify { userRepo.findByIdOrNull("test-id") }
    }
}
