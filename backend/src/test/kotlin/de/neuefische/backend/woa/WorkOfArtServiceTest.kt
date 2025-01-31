package de.neuefische.backend.woa

import de.neuefische.backend.common.Medium
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.bson.BsonObjectId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull

class WorkOfArtServiceTest {

    private val workOfArtRepo = mockk<WorkOfArtRepo>()
    private val workOfArtService = WorkOfArtService(workOfArtRepo)

    @Test
    fun getWorkOfArtById() {

        // Given
        val yellowCadmium24 = Material(
            name = "Yellow Cadmium 24",
            identifier = "24",
            brand = "Schmincke",
            line = "Horadam",
            type = "Half Pan",
            medium = Medium.WATERCOLORS,
        )
        val paintbrush = Material(
            // This one has some missing fields
            name = "Round Brush",
            brand = "Da Vinci",
            line = "Maestro",
            type = "Paintbrush",
        )

        val workOfArt = WorkOfArt(
            id = BsonObjectId(),
            user = BsonObjectId(),
            challengeId = BsonObjectId(),
            title = "test-title",
            description = "test-description",
            imageUrl = "test-image-url",
            medium = Medium.WATERCOLORS,
            materials = listOf(yellowCadmium24, paintbrush),
        )

        val expectedResponse = WorkOfArtResponse(
            id = workOfArt.id.value.toString(),
            user = workOfArt.user.value.toString(),
            challengeId = workOfArt.challengeId?.value.toString(),
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
            )
        )

        every { workOfArtRepo.findByIdOrNull(workOfArt.id.value.toString()) } returns workOfArt

        // When
        val result = workOfArtService.getWorkOfArtById(workOfArt.id.value.toString())

        // Then
        assertEquals(expectedResponse, result)
    }

    @Test
    fun getWorkOfArtByIdWithOnlyRequiredFields() {

        // Given
        val workOfArt = WorkOfArt(
            id = BsonObjectId(),
            user = BsonObjectId(),
            title = "test-title",
            imageUrl = "test-image-url",
            medium = Medium.PAN_PASTELS,
        )

        val expectedResponse = WorkOfArtResponse(
            id = workOfArt.id.value.toString(),
            user = workOfArt.user.value.toString(),
            title = "test-title",
            imageUrl = "test-image-url",
            medium = "pan pastels",
        )

        every { workOfArtRepo.findByIdOrNull(workOfArt.id.value.toString()) } returns workOfArt

        // When
        val result = workOfArtService.getWorkOfArtById(workOfArt.id.value.toString())

        // Then
        assertEquals(expectedResponse, result)
    }
}
