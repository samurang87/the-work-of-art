package de.neuefische.backend.user

import de.neuefische.backend.common.Medium
import io.mockk.every
import io.mockk.mockk
import org.bson.BsonObjectId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UserServiceTest {

    private val userRepo = mockk<UserRepo>()
    private val userService = UserService(userRepo)

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

        val expectedResponse = UserProfileResponse(
            id = user.id.value.toString(),
            name = "test-user",
            bio = "test-bio",
            imageUrl = "test-image-url",
            mediums = listOf("watercolor", "ink")
        )

        every { userRepo.findByName("test-user") } returns user

        // When
        val result = userService.getUserByName("test-user")

        // Then
        assertEquals(expectedResponse, result)
    }
}
