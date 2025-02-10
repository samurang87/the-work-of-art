package de.neuefische.backend.woa

import de.neuefische.backend.common.Medium
import org.bson.BsonObjectId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class WorkOfArtControllerTest {
    @TestConfiguration
    class TestConfig {
        @Bean
        fun clock(): Clock =
            Clock.fixed(
                LocalDateTime
                    .parse("2025-02-10T11:56:32.966")
                    .toInstant(ZoneOffset.UTC),
                ZoneOffset.UTC,
            )
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var workOfArtRepo: WorkOfArtRepo

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")

    @Autowired
    private lateinit var clock: Clock

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

    private lateinit var yellowSunset: WorkOfArt
    private lateinit var blueDawn: WorkOfArt
    private lateinit var redMidday: WorkOfArt

    @BeforeEach
    fun setUp() {
        workOfArtRepo.deleteAll()

        yellowSunset =
            WorkOfArt(
                id = BsonObjectId(),
                user = BsonObjectId(),
                challengeId = BsonObjectId(),
                userName = "max_mustermann",
                title = "Yellow Sunset",
                description = "a yellow sunset",
                imageUrl = "https://example.com/yellow-sunset.jpg",
                medium = Medium.WATERCOLORS,
                materials =
                    listOf(
                        yellowCadmium24,
                        paintbrush,
                    ),
                createdAt = LocalDateTime.now(clock).plusHours(0),
            )

        blueDawn =
            WorkOfArt(
                id = BsonObjectId(),
                user = BsonObjectId(),
                userName = "mario_rossi",
                title = "Blue Dawn",
                imageUrl = "https://example.com/blue-dawn.jpg",
                medium = Medium.GOUACHE,
                createdAt = LocalDateTime.now(clock).plusHours(1),
            )

        redMidday =
            WorkOfArt(
                id = BsonObjectId(),
                user = BsonObjectId(),
                challengeId = BsonObjectId(),
                userName = "jane_snow",
                title = "Red Midday",
                description = "a red midday",
                imageUrl = "https://example.com/red-midday.jpg",
                medium = Medium.WATERCOLORS,
                materials =
                    listOf(
                        scarletRed12,
                        paintbrush,
                    ),
                createdAt = LocalDateTime.now(clock).plusHours(2),
            )
        workOfArtRepo.save(yellowSunset)
        workOfArtRepo.save(blueDawn)
        workOfArtRepo.save(redMidday)
    }

    // GET /api/woa/{id}
    @Test
    fun `should return work of art by id`() {
        // When
        val result = mockMvc.perform(get("/api/woa/${yellowSunset.id.value}"))

        // Then
        result
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(yellowSunset.id.value.toString()))
            .andExpect(jsonPath("$.user").value(yellowSunset.user.value.toString()))
            .andExpect(jsonPath("$.challengeId").value(yellowSunset.challengeId?.value.toString()))
            .andExpect(jsonPath("$.userName").value("max_mustermann"))
            .andExpect(jsonPath("$.title").value("Yellow Sunset"))
            .andExpect(jsonPath("$.description").value("a yellow sunset"))
            .andExpect(jsonPath("$.imageUrl").value("https://example.com/yellow-sunset.jpg"))
            .andExpect(jsonPath("$.medium").value("watercolors"))
            .andExpect(jsonPath("$.materials").isArray)
            .andExpect(jsonPath("$.materials[0].name").value("Yellow Cadmium 24"))
            .andExpect(jsonPath("$.materials[0].identifier").value("24"))
            .andExpect(jsonPath("$.materials[0].brand").value("Schmincke"))
            .andExpect(jsonPath("$.materials[0].line").value("Horadam"))
            .andExpect(jsonPath("$.materials[0].type").value("Half Pan"))
            .andExpect(jsonPath("$.materials[0].medium").value("watercolors"))
            .andExpect(jsonPath("$.materials[1].name").value("Round Brush"))
            .andExpect(jsonPath("$.materials[1].identifier").doesNotExist())
            .andExpect(jsonPath("$.materials[1].brand").value("Da Vinci"))
            .andExpect(jsonPath("$.materials[1].line").value("Maestro"))
            .andExpect(jsonPath("$.materials[1].type").value("Paintbrush"))
            .andExpect(jsonPath("$.materials[1].medium").doesNotExist())
            .andExpect(
                jsonPath("$.createdAt").value(
                    yellowSunset.createdAt.format(
                        formatter,
                    ),
                ),
            )
    }

    @Test
    fun `should return not found for non-existent work of art id`() {
        // Given
        val workOfArtId = BsonObjectId()

        // When
        val result = mockMvc.perform(get("/api/woa/${workOfArtId.value}"))

        // Then
        result.andExpect(status().isNotFound)
    }

    @Test
    fun `should return work of art by id with only required fields`() {
        // When
        val result = mockMvc.perform(get("/api/woa/${blueDawn.id.value}"))

        // Then
        result
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(blueDawn.id.value.toString()))
            .andExpect(jsonPath("$.user").value(blueDawn.user.value.toString()))
            .andExpect(jsonPath("$.userName").value("mario_rossi"))
            .andExpect(jsonPath("$.challengeId").doesNotExist())
            .andExpect(jsonPath("$.title").value("Blue Dawn"))
            .andExpect(jsonPath("$.description").doesNotExist())
            .andExpect(jsonPath("$.imageUrl").value("https://example.com/blue-dawn.jpg"))
            .andExpect(jsonPath("$.medium").value("gouache"))
            .andExpect(jsonPath("$.materials").isEmpty())
            .andExpect(jsonPath("$.createdAt").value(blueDawn.createdAt.format(formatter)))
    }

    // GET /api/woa/
    @Test
    fun `should return all works of art when no filters are applied`() {
        // When
        val result = mockMvc.perform(get("/api/woa"))

        // Then
        result
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(redMidday.id.value.toString()))
            .andExpect(jsonPath("$[0].user").value(redMidday.user.value.toString()))
            .andExpect(jsonPath("$[0].userName").value("jane_snow"))
            .andExpect(jsonPath("$[0].title").value("Red Midday"))
            .andExpect(jsonPath("$[0].imageUrl").value("https://example.com/red-midday.jpg"))
            .andExpect(jsonPath("$[0].medium").value("watercolors"))
            .andExpect(
                jsonPath("$[0].createdAt").value(
                    redMidday.createdAt.format(
                        formatter,
                    ),
                ),
            ).andExpect(jsonPath("$[1].id").value(blueDawn.id.value.toString()))
            .andExpect(jsonPath("$[1].user").value(blueDawn.user.value.toString()))
            .andExpect(jsonPath("$[1].userName").value("mario_rossi"))
            .andExpect(jsonPath("$[1].title").value("Blue Dawn"))
            .andExpect(jsonPath("$[1].imageUrl").value("https://example.com/blue-dawn.jpg"))
            .andExpect(jsonPath("$[1].medium").value("gouache"))
            .andExpect(
                jsonPath("$[1].createdAt").value(
                    blueDawn.createdAt.format(
                        formatter,
                    ),
                ),
            ).andExpect(jsonPath("$[2].id").value(yellowSunset.id.value.toString()))
            .andExpect(jsonPath("$[2].user").value(yellowSunset.user.value.toString()))
            .andExpect(jsonPath("$[2].userName").value("max_mustermann"))
            .andExpect(jsonPath("$[2].title").value("Yellow Sunset"))
            .andExpect(jsonPath("$[2].imageUrl").value("https://example.com/yellow-sunset.jpg"))
            .andExpect(jsonPath("$[2].medium").value("watercolors"))
            .andExpect(
                jsonPath("$[2].createdAt").value(
                    yellowSunset.createdAt.format(
                        formatter,
                    ),
                ),
            )
    }

    @Test
    fun `should return works of art filtered by mediums`() {
        // When
        val result = mockMvc.perform(get("/api/woa?mediums=watercolors"))

        // Then
        result
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(redMidday.id.value.toString()))
            .andExpect(jsonPath("$[0].user").value(redMidday.user.value.toString()))
            .andExpect(jsonPath("$[0].userName").value("jane_snow"))
            .andExpect(jsonPath("$[0].title").value("Red Midday"))
            .andExpect(jsonPath("$[0].imageUrl").value("https://example.com/red-midday.jpg"))
            .andExpect(jsonPath("$[0].medium").value("watercolors"))
            .andExpect(
                jsonPath("$[0].createdAt").value(
                    redMidday.createdAt.format(
                        formatter,
                    ),
                ),
            ).andExpect(jsonPath("$[1].id").value(yellowSunset.id.value.toString()))
            .andExpect(jsonPath("$[1].user").value(yellowSunset.user.value.toString()))
            .andExpect(jsonPath("$[1].userName").value("max_mustermann"))
            .andExpect(jsonPath("$[1].title").value("Yellow Sunset"))
            .andExpect(jsonPath("$[1].imageUrl").value("https://example.com/yellow-sunset.jpg"))
            .andExpect(jsonPath("$[1].medium").value("watercolors"))
            .andExpect(
                jsonPath("$[1].createdAt").value(
                    yellowSunset.createdAt.format(
                        formatter,
                    ),
                ),
            )
    }
}
