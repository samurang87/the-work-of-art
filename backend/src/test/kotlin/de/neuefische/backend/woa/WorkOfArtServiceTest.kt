package de.neuefische.backend.woa

import de.neuefische.backend.common.Medium
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.bson.BsonObjectId
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.AccessDeniedException

class WorkOfArtServiceTest {
    private val workOfArtRepo = mockk<WorkOfArtRepo>()
    private val workOfArtService = WorkOfArtService(workOfArtRepo)

    // Reusable test data
    private val yellowCadmium24 =
        Material(
            name = "Yellow Cadmium 24",
            identifier = "24",
            brand = "Schmincke",
            line = "Horadam",
            type = "Half Pan",
            medium = Medium.WATERCOLORS,
        )
    private val scarletRed12 =
        Material(
            name = "Scarlet Red",
            identifier = "12",
            brand = "Schmincke",
            line = "Akademie",
            type = "Tube",
            medium = Medium.WATERCOLORS,
        )
    private val violet23 =
        Material(
            name = "Violet",
            identifier = "23",
            brand = "Schmincke",
            line = "Akademie",
            type = "Full Pan",
            medium = Medium.WATERCOLORS,
        )
    private val paintbrush =
        Material(
            // This one has some missing fields
            name = "Round Brush",
            brand = "Da Vinci",
            line = "Maestro",
            type = "Paintbrush",
        )

    private val yellowSunset =
        WorkOfArt(
            id = BsonObjectId(),
            user = BsonObjectId(),
            challengeId = BsonObjectId(),
            userName = "max_mustermann",
            title = "Yellow Sunset",
            description = "a yellow sunset",
            imageUrl = "https://example.com/yellow-sunset.jpg",
            medium = Medium.WATERCOLORS,
            materials = listOf(yellowCadmium24, paintbrush),
        )

    private val blueDawn =
        WorkOfArt(
            id = BsonObjectId(),
            user = BsonObjectId(),
            userName = "mario_rossi",
            title = "Blue Dawn",
            imageUrl = "https://example.com/blue-dawn.jpg",
            medium = Medium.PAN_PASTELS,
        )

    private val redMiddayUserId = BsonObjectId()
    private val redMiddayUserName = "jane_snow"

    private val redMidday =
        WorkOfArt(
            id = BsonObjectId(),
            user = redMiddayUserId,
            challengeId = BsonObjectId(),
            userName = redMiddayUserName,
            title = "Red Midday",
            description = "a red midday",
            imageUrl = "https://example.com/red-midday.jpg",
            medium = Medium.WATERCOLORS,
            materials = listOf(scarletRed12, paintbrush),
        )

    private val violetStarlight =
        WorkOfArt(
            id = BsonObjectId(),
            user = redMiddayUserId,
            challengeId = BsonObjectId(),
            userName = redMiddayUserName,
            title = "Violet Starlight",
            description = "a night brightened by stars",
            imageUrl = "https://example.com/violet-starlight.jpg",
            medium = Medium.WATERCOLORS,
            materials = listOf(violet23, paintbrush),
        )

    @Test
    fun `should return work of art by id`() {
        // Given
        val expectedResponse =
            WorkOfArtResponse(
                id = yellowSunset.id.value.toString(),
                user = yellowSunset.user.value.toString(),
                challengeId = yellowSunset.challengeId?.value.toString(),
                userName = "max_mustermann",
                title = "Yellow Sunset",
                description = "a yellow sunset",
                imageUrl = "https://example.com/yellow-sunset.jpg",
                medium = "watercolors",
                materials =
                    listOf(
                        MaterialDAO(
                            name = "Yellow Cadmium 24",
                            identifier = "24",
                            brand = "Schmincke",
                            line = "Horadam",
                            type = "Half Pan",
                            medium = "watercolors",
                        ),
                        MaterialDAO(
                            name = "Round Brush",
                            brand = "Da Vinci",
                            line = "Maestro",
                            type = "Paintbrush",
                        ),
                    ),
                createdAt = yellowSunset.createdAt.toString(),
            )

        every { workOfArtRepo.findByIdOrNull(yellowSunset.id.value.toString()) } returns yellowSunset

        // When
        val result = workOfArtService.getWorkOfArtById(yellowSunset.id.value.toString())

        // Then
        assertEquals(expectedResponse, result)
    }

    @Test
    fun `should return work of art by id with only required fields`() {
        // Given
        val expectedResponse =
            WorkOfArtResponse(
                id = blueDawn.id.value.toString(),
                user = blueDawn.user.value.toString(),
                userName = "mario_rossi",
                title = "Blue Dawn",
                imageUrl = "https://example.com/blue-dawn.jpg",
                medium = "pan pastels",
                createdAt = blueDawn.createdAt.toString(),
            )

        every { workOfArtRepo.findByIdOrNull(blueDawn.id.value.toString()) } returns blueDawn

        // When
        val result = workOfArtService.getWorkOfArtById(blueDawn.id.value.toString())

        // Then
        assertEquals(expectedResponse, result)
    }

    @Test
    fun `should return all works of art when no mediums or user specified`() {
        // Given
        val expectedResponse =
            listOf(
                WorkOfArtShortResponse(
                    id = redMidday.id.value.toString(),
                    user = redMidday.user.value.toString(),
                    userName = "jane_snow",
                    title = "Red Midday",
                    imageUrl = "https://example.com/red-midday.jpg",
                    medium = "watercolors",
                    createdAt = redMidday.createdAt.toString(),
                ),
                WorkOfArtShortResponse(
                    id = blueDawn.id.value.toString(),
                    user = blueDawn.user.value.toString(),
                    userName = "mario_rossi",
                    title = "Blue Dawn",
                    imageUrl = "https://example.com/blue-dawn.jpg",
                    medium = "pan pastels",
                    createdAt = blueDawn.createdAt.toString(),
                ),
                WorkOfArtShortResponse(
                    id = yellowSunset.id.value.toString(),
                    user = yellowSunset.user.value.toString(),
                    userName = "max_mustermann",
                    title = "Yellow Sunset",
                    imageUrl = "https://example.com/yellow-sunset.jpg",
                    medium = "watercolors",
                    createdAt = yellowSunset.createdAt.toString(),
                ),
            )

        every { workOfArtRepo.findAll() } returns
            listOf(
                redMidday,
                yellowSunset,
                blueDawn,
            )

        // When
        val result = workOfArtService.getAllWorksOfArt()

        // Then
        assertEquals(expectedResponse, result)
    }

    @Test
    fun `should return all works of art with specified mediums`() {
        // Given
        val expectedResponse =
            listOf(
                WorkOfArtShortResponse(
                    id = redMidday.id.value.toString(),
                    user = redMidday.user.value.toString(),
                    userName = "jane_snow",
                    title = "Red Midday",
                    imageUrl = "https://example.com/red-midday.jpg",
                    medium = "watercolors",
                    createdAt = redMidday.createdAt.toString(),
                ),
                WorkOfArtShortResponse(
                    id = yellowSunset.id.value.toString(),
                    user = yellowSunset.user.value.toString(),
                    userName = "max_mustermann",
                    title = "Yellow Sunset",
                    imageUrl = "https://example.com/yellow-sunset.jpg",
                    medium = "watercolors",
                    createdAt = yellowSunset.createdAt.toString(),
                ),
            )

        every { workOfArtRepo.findAllByMediumIn(listOf(Medium.WATERCOLORS)) } returns
            listOf(
                redMidday,
                yellowSunset,
            )

        // When
        val result = workOfArtService.getAllWorksOfArt(listOf(Medium.WATERCOLORS))

        // Then
        assertEquals(expectedResponse, result)
    }

    @Test
    fun `should return all works of art with specified user`() {
        // Given
        val expectedResponse =
            listOf(
                WorkOfArtShortResponse(
                    id = violetStarlight.id.value.toString(),
                    user = redMidday.user.value.toString(),
                    userName = "jane_snow",
                    title = "Violet Starlight",
                    imageUrl = "https://example.com/violet-starlight.jpg",
                    medium = "watercolors",
                    createdAt = violetStarlight.createdAt.toString(),
                ),
                WorkOfArtShortResponse(
                    id = redMidday.id.value.toString(),
                    user = redMidday.user.value.toString(),
                    userName = "jane_snow",
                    title = "Red Midday",
                    imageUrl = "https://example.com/red-midday.jpg",
                    medium = "watercolors",
                    createdAt = redMidday.createdAt.toString(),
                ),
            )

        every { workOfArtRepo.findAllByUser(redMiddayUserId) } returns
            listOf(
                redMidday,
                violetStarlight,
            )

        // When
        val result =
            workOfArtService.getAllWorksOfArtByUser(redMiddayUserId.value.toString())

        // Then
        assertEquals(expectedResponse, result)
    }

    @Test
    fun `should return nothing when user has not published anything`() {
        // Given
        val userId = BsonObjectId()
        every { workOfArtRepo.findAllByUser(userId) } returns emptyList()

        // When
        val result = workOfArtService.getAllWorksOfArtByUser(userId.value.toString())

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `should create new work of art with all fields`() {
        // Given
        val userIdAsString = BsonObjectId().value.toString()
        val challengeIdAsString = BsonObjectId().value.toString()

        val request =
            WorkOfArtCreateOrUpdateRequest(
                user = userIdAsString,
                userName = "someUserName",
                challengeId = challengeIdAsString,
                title = "Some Title",
                description = "Some Description",
                imageUrl = "https://example.com/some-image.jpg",
                medium = "watercolors",
                materials =
                    listOf(
                        MaterialDAO(
                            name = "Green Chromium 24",
                            identifier = "24",
                            brand = "Schmincke",
                            line = "Horadam",
                            type = "Half Pan",
                            medium = "watercolors",
                        ),
                        MaterialDAO(
                            name = "Round Brush",
                            brand = "Da Vinci",
                            line = "Maestro",
                            type = "Paintbrush",
                        ),
                    ),
            )

        val expectedWorkOfArt =
            WorkOfArt(
                user = BsonObjectId(ObjectId(userIdAsString)),
                userName = request.userName,
                challengeId = BsonObjectId(ObjectId(challengeIdAsString)),
                title = request.title,
                description = request.description,
                imageUrl = request.imageUrl,
                medium = Medium.WATERCOLORS,
                materials =
                    listOf(
                        Material(
                            name = "Green Chromium 24",
                            identifier = "24",
                            brand = "Schmincke",
                            line = "Horadam",
                            type = "Half Pan",
                            medium = Medium.WATERCOLORS,
                        ),
                        Material(
                            name = "Round Brush",
                            brand = "Da Vinci",
                            line = "Maestro",
                            type = "Paintbrush",
                        ),
                    ),
            )

        every { workOfArtRepo.save(any()) } returns expectedWorkOfArt

        // When
        val result = workOfArtService.createWorkOfArt(request)

        // Then
        assertEquals(true, result.id.isNotBlank())
        assertEquals(expectedWorkOfArt.user.value.toString(), result.user)
        assertEquals(expectedWorkOfArt.userName, result.userName)
        assertEquals(
            expectedWorkOfArt.challengeId?.value.toString(),
            result.challengeId,
        )
        assertEquals(expectedWorkOfArt.title, result.title)
        assertEquals(expectedWorkOfArt.description, result.description)
        assertEquals(expectedWorkOfArt.imageUrl, result.imageUrl)
        assertEquals(expectedWorkOfArt.medium.lowercase, result.medium)
        assertEquals(
            listOf(
                MaterialDAO(
                    name = "Green Chromium 24",
                    identifier = "24",
                    brand = "Schmincke",
                    line = "Horadam",
                    type = "Half Pan",
                    medium = "watercolors",
                ),
                MaterialDAO(
                    name = "Round Brush",
                    brand = "Da Vinci",
                    line = "Maestro",
                    type = "Paintbrush",
                ),
            ),
            result.materials,
        )
    }

    @Test
    fun `should create new work of art with only required fields`() {
        // Given
        val userIdAsString = BsonObjectId().value.toString()
        val request =
            WorkOfArtCreateOrUpdateRequest(
                user = userIdAsString,
                userName = "someUserName",
                title = "Some Title",
                imageUrl = "https://example.com/some-image.jpg",
                medium = "watercolors",
            )

        val expectedWorkOfArt =
            WorkOfArt(
                user = BsonObjectId(ObjectId(userIdAsString)),
                userName = request.userName,
                title = request.title,
                imageUrl = request.imageUrl,
                medium = Medium.WATERCOLORS,
                materials = emptyList(),
            )

        every { workOfArtRepo.save(any()) } returns expectedWorkOfArt

        // When
        val result = workOfArtService.createWorkOfArt(request)

        // Then
        assertEquals(true, result.id.isNotBlank())
        assertEquals(expectedWorkOfArt.user.value.toString(), result.user)
        assertEquals(expectedWorkOfArt.userName, result.userName)
        assertEquals(null, result.challengeId)
        assertEquals(null, result.description)
        assertEquals(expectedWorkOfArt.title, result.title)
        assertEquals(expectedWorkOfArt.imageUrl, result.imageUrl)
        assertEquals(expectedWorkOfArt.medium.lowercase, result.medium)
        assertEquals(emptyList<MaterialDAO>(), result.materials)
    }

    @Test
    fun `should edit existing work of art with all fields`() {
        // Given
        val request =
            WorkOfArtCreateOrUpdateRequest(
                user = yellowSunset.user.value.toString(),
                userName = yellowSunset.userName,
                challengeId = yellowSunset.challengeId?.value.toString(),
                title = "Updated Title",
                description = "Updated Description",
                imageUrl = "https://example.com/updated-image.jpg",
                medium = "pencils",
                materials =
                    listOf(
                        MaterialDAO(
                            name = "Updated Material",
                            identifier = "99",
                            brand = "Updated Brand",
                            line = "Updated Line",
                            type = "Updated Type",
                            medium = "pencils",
                        ),
                    ),
            )

        val updatedWorkOfArt =
            yellowSunset.copy(
                title = request.title,
                description = request.description,
                imageUrl = request.imageUrl,
                medium = Medium.PENCILS,
                materials =
                    listOf(
                        Material(
                            name = "Updated Material",
                            identifier = "99",
                            brand = "Updated Brand",
                            line = "Updated Line",
                            type = "Updated Type",
                            medium = Medium.PENCILS,
                        ),
                    ),
            )

        every { workOfArtRepo.findByIdOrNull(yellowSunset.id.value.toString()) } returns yellowSunset
        every { workOfArtRepo.save(any()) } returns updatedWorkOfArt

        // When
        val result =
            workOfArtService.updateWorkOfArt(
                yellowSunset.id.value.toString(),
                request,
                yellowSunset.userName,
            )

        // Then
        val expectedResponse =
            WorkOfArtResponse(
                id = yellowSunset.id.value.toString(),
                user = yellowSunset.user.value.toString(),
                challengeId = yellowSunset.challengeId?.value.toString(),
                userName = yellowSunset.userName,
                title = "Updated Title",
                description = "Updated Description",
                imageUrl = "https://example.com/updated-image.jpg",
                medium = "pencils",
                materials =
                    listOf(
                        MaterialDAO(
                            name = "Updated Material",
                            identifier = "99",
                            brand = "Updated Brand",
                            line = "Updated Line",
                            type = "Updated Type",
                            medium = "pencils",
                        ),
                    ),
                createdAt = yellowSunset.createdAt.toString(),
            )
        assertEquals(expectedResponse, result)
    }

    @Test
    fun `should throw error when editing nonexistent work of art`() {
        // Given
        val nonexistentId = BsonObjectId().value.toString()
        val request =
            WorkOfArtCreateOrUpdateRequest(
                user = "someUserId",
                userName = "someUserName",
                title = "Some Title",
                imageUrl = "https://example.com/some-image.jpg",
                medium = "watercolors",
            )

        every { workOfArtRepo.findByIdOrNull(nonexistentId) } returns null

        // When / Then
        val exception =
            assertThrows<IllegalArgumentException> {
                workOfArtService.updateWorkOfArt(nonexistentId, request, "someUserName")
            }
        assertEquals("Work of Art not found", exception.message)
    }

    @Test
    fun `should throw error when editing another user's work of art`() {
        // Given
        val request =
            WorkOfArtCreateOrUpdateRequest(
                user = yellowSunset.user.value.toString(),
                userName = yellowSunset.userName,
                title = "Updated Title",
                imageUrl = "https://example.com/updated-image.jpg",
                medium = "pencils",
                materials =
                    listOf(
                        MaterialDAO(
                            name = "Updated Material",
                            identifier = "99",
                            brand = "Updated Brand",
                            line = "Updated Line",
                            type = "Updated Type",
                            medium = "pencils",
                        ),
                    ),
            )

        every { workOfArtRepo.findByIdOrNull(yellowSunset.id.value.toString()) } returns yellowSunset

        // When / Then
        assertThrows<AccessDeniedException> {
            workOfArtService.updateWorkOfArt(
                yellowSunset.id.value.toString(),
                request,
                "someOtherUserName",
            )
        }
        verify(exactly = 0) { workOfArtRepo.save(any()) }
    }

    @Test
    fun `should delete work of art`() {
        // Given
        val id = yellowSunset.id.value.toString()
        every { workOfArtRepo.findByIdOrNull(id) } returns yellowSunset
        every { workOfArtRepo.deleteById(id) } returns Unit

        // When
        val res =
            workOfArtService.deleteWorkOfArt(
                yellowSunset.id.value.toString(),
                yellowSunset.userName,
            )

        // Then
        assertEquals(res, id)
        verify { workOfArtRepo.deleteById(id) }
    }

    @Test
    fun `should throw error when deleting nonexistent work of art`() {
        // Given
        val nonexistentId = BsonObjectId().value.toString()
        every { workOfArtRepo.findByIdOrNull(nonexistentId) } returns null

        // When / Then
        assertThrows<IllegalArgumentException> {
            workOfArtService.deleteWorkOfArt(nonexistentId, "someUserName")
        }

        verify(exactly = 0) { workOfArtRepo.deleteById(nonexistentId) }
    }

    @Test
    fun `should throw error when deleting different user's work of art`() {
        // Given
        val id = yellowSunset.id.value.toString()
        every { workOfArtRepo.findByIdOrNull(id) } returns yellowSunset

        // When / Then
        assertThrows<AccessDeniedException> {
            workOfArtService.deleteWorkOfArt(id, "someOtherUserName")
        }

        verify(exactly = 0) { workOfArtRepo.deleteById(id) }
    }
}
