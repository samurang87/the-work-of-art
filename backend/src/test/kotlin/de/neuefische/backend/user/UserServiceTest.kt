package de.neuefische.backend.user

import de.neuefische.backend.common.Medium
import de.neuefische.backend.exceptions.NotFoundException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.bson.BsonObjectId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.data.repository.findByIdOrNull
import java.util.stream.Stream
import kotlin.test.assertFailsWith

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

    @Test
    fun `should create user if it does not exist`() {
        // Given
        every { userRepo.findByName("test-user") } returns null
        every { userRepo.save(any()) } answers { firstArg() }

        // When
        val result: User = userService.findOrCreateUser("test-user")

        // Then
        assertEquals("test-user", result.name)
        verify { userRepo.save(any()) }
    }

    @Test
    fun `should return user if it exists`() {
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

        every { userRepo.findByName("test-user") } returns user

        // When
        val result: User = userService.findOrCreateUser("test-user")

        // Then
        assertEquals("test-user", result.name)
        verify(exactly = 0) { userRepo.save(any()) }
    }

    companion object {
        @JvmStatic
        fun provideUpdateRequests(): Stream<Arguments> =
            Stream.of(
                Arguments.of(
                    UserProfileUpdateRequest(
                        bio = "updated-bio",
                        imageUrl = "updated-image-url",
                        mediums = listOf("ink"),
                    ),
                    listOf(Medium.INK),
                ),
                Arguments.of(
                    UserProfileUpdateRequest(
                        bio = null,
                        imageUrl = "updated-image-url",
                        mediums = listOf("ink"),
                    ),
                    listOf(Medium.INK),
                ),
                Arguments.of(
                    UserProfileUpdateRequest(
                        bio = "updated-bio",
                        imageUrl = null,
                        mediums = listOf("ink"),
                    ),
                    listOf(Medium.INK),
                ),
                Arguments.of(
                    UserProfileUpdateRequest(
                        bio = "updated-bio",
                        imageUrl = "updated-image-url",
                        mediums = emptyList(),
                    ),
                    emptyList<Medium>(),
                ),
            )
    }

    @ParameterizedTest
    @MethodSource("provideUpdateRequests")
    fun `should update user with various payloads`(
        updateRequest: UserProfileUpdateRequest,
        expectedMediums: List<Medium>,
    ) {
        // Given
        val userId = BsonObjectId()
        val existingUser =
            User(
                id = userId,
                name = "existing-user",
                bio = "existing-bio",
                imageUrl = "existing-image-url",
                mediums = listOf(Medium.WATERCOLORS),
            )
        val expectedUser =
            existingUser.copy(
                bio = updateRequest.bio,
                imageUrl = updateRequest.imageUrl,
                mediums = expectedMediums,
            )

        every { userRepo.findByIdOrNull(userId.toString()) } returns existingUser
        every { userRepo.save(any()) } returns expectedUser

        // When
        val result = userService.updateUser(userId.toString(), updateRequest)

        // Then
        assertEquals(expectedUser, result)
        verify { userRepo.save(expectedUser) }
    }

    @Test
    fun `should throw exception for nonexistent user`() {
        // Given
        val userId = BsonObjectId().value.toString()
        val updateRequest =
            UserProfileUpdateRequest(
                bio = "updated-bio",
                imageUrl = "updated-image-url",
                mediums = listOf("ink"),
            )

        every { userRepo.findByIdOrNull(userId) } returns null

        // When / Then
        val exception =
            assertFailsWith<NotFoundException> {
                userService.updateUser(userId, updateRequest)
            }
        assertEquals("User $userId not found", exception.message)
        verify(exactly = 0) { userRepo.save(any()) }
    }
}
