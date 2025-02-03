package de.neuefische.backend.woa

import de.neuefische.backend.common.Medium
import io.mockk.every
import io.mockk.mockk
import org.bson.BsonObjectId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull

class WorkOfArtServiceTest {

    private val workOfArtRepo = mockk<WorkOfArtRepo>()
    private val workOfArtService = WorkOfArtService(workOfArtRepo)

    // Reusable test data
    private val yellowCadmium24 = Material(
        name = "Yellow Cadmium 24",
        identifier = "24",
        brand = "Schmincke",
        line = "Horadam",
        type = "Half Pan",
        medium = Medium.WATERCOLORS,
    )
    private val paintbrush = Material(
        // This one has some missing fields
        name = "Round Brush",
        brand = "Da Vinci",
        line = "Maestro",
        type = "Paintbrush",
    )

    private val yellowSunset = WorkOfArt(
        id = BsonObjectId(),
        user = BsonObjectId(),
        challengeId = BsonObjectId(),
        title = "test-title",
        description = "test-description",
        imageUrl = "test-image-url",
        medium = Medium.WATERCOLORS,
        materials = listOf(yellowCadmium24, paintbrush),
    )

    private val blueDawn = WorkOfArt(
        id = BsonObjectId(),
        user = BsonObjectId(),
        title = "test-title",
        imageUrl = "test-image-url",
        medium = Medium.PAN_PASTELS,
    )

    @Test
    fun getWorkOfArtById() {

        // Given
        val expectedResponse = WorkOfArtResponse(
            id = yellowSunset.id.value.toString(),
            user = yellowSunset.user.value.toString(),
            challengeId = yellowSunset.challengeId?.value.toString(),
            title = "test-title",
            description = "test-description",
            imageUrl = "test-image-url",
            medium = "watercolors",
            materials = listOf(
                MaterialResponse(
                    name = "Yellow Cadmium 24",
                    identifier = "24",
                    brand = "Schmincke",
                    line = "Horadam",
                    type = "Half Pan",
                    medium = "watercolors",
                ),
                MaterialResponse(
                    name = "Round Brush",
                    brand = "Da Vinci",
                    line = "Maestro",
                    type = "Paintbrush",
                )
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
    fun getWorkOfArtByIdWithOnlyRequiredFields() {

        // Given
        val expectedResponse = WorkOfArtResponse(
            id = blueDawn.id.value.toString(),
            user = blueDawn.user.value.toString(),
            title = "test-title",
            imageUrl = "test-image-url",
            medium = "pan pastels",
            createdAt = blueDawn.createdAt.toString(),
        )

        every { workOfArtRepo.findByIdOrNull(blueDawn.id.value.toString()) } returns blueDawn

        // When
        val result = workOfArtService.getWorkOfArtById(blueDawn.id.value.toString())

        // Then
        assertEquals(expectedResponse, result)
    }
}
