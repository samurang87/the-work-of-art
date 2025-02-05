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

    private val redMidday =
        WorkOfArt(
            id = BsonObjectId(),
            user = BsonObjectId(),
            challengeId = BsonObjectId(),
            userName = "jane_snow",
            title = "Red Midday",
            description = "a red midday",
            imageUrl = "https://example.com/red-midday.jpg",
            medium = Medium.WATERCOLORS,
            materials = listOf(scarletRed12, paintbrush),
        )

    @Test
    fun getWorkOfArtById() {
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
    fun getWorkOfArtByIdWithOnlyRequiredFields() {
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
    fun getAllWorksOfArtNoMediumsSpecified() {
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

        every { workOfArtRepo.findAll() } returns listOf(redMidday, yellowSunset, blueDawn)

        // When
        val result = workOfArtService.getAllWorksOfArt()

        // Then
        assertEquals(expectedResponse, result)
    }

    @Test
    fun getAllWorksOfArtWithMediumsSpecified() {
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

        every { workOfArtRepo.findAllByMediumIn(listOf(Medium.WATERCOLORS)) } returns listOf(redMidday, yellowSunset)

        // When
        val result = workOfArtService.getAllWorksOfArt(listOf(Medium.WATERCOLORS))

        // Then
        assertEquals(expectedResponse, result)
    }
}
