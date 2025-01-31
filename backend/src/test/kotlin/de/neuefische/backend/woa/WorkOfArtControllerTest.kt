package de.neuefische.backend.woa

import com.ninjasquad.springmockk.MockkBean
import de.neuefische.backend.common.Medium
import io.mockk.every
import org.bson.BsonObjectId
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class WorkOfArtControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var workOfArtRepo: WorkOfArtRepo

    // GET /api/woa/{id}
    @Test
    fun getWorkOfArtById() {

        // Given
        val workOfArtId = BsonObjectId()
        val workOfArtUserId = BsonObjectId()
        val workOfArtChallengeId = BsonObjectId()

        val workOfArt = WorkOfArt(
            id = workOfArtId,
            user = workOfArtUserId,
            challengeId = workOfArtChallengeId,
            title = "test-title",
            description = "test-description",
            imageUrl = "test-image-url",
            medium = Medium.WATERCOLORS,
            materials = listOf(
                Material(
                    name = "Yellow Cadmium 24",
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

        every { workOfArtRepo.findByIdOrNull(workOfArtId.value.toString()) } returns workOfArt

        // When
        val result = mockMvc.perform(get("/api/woa/${workOfArtId.value}"))

        // Then
        result.andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(workOfArtId.value.toString()))
            .andExpect(jsonPath("$.user").value(workOfArtUserId.value.toString()))
            .andExpect(jsonPath("$.challengeId").value(workOfArtChallengeId.value.toString()))
            .andExpect(jsonPath("$.title").value("test-title"))
            .andExpect(jsonPath("$.description").value("test-description"))
            .andExpect(jsonPath("$.imageUrl").value("test-image-url"))
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
    }

    @Test
    fun getWorkOfArtByIdReturnsNotFound() {

        // Given
        val workOfArtId = BsonObjectId()

        every { workOfArtRepo.findByIdOrNull(workOfArtId.value.toString()) } returns null

        // When
        val result = mockMvc.perform(get("/api/woa/${workOfArtId.value}"))

        // Then
        result.andExpect(status().isNotFound)
    }

    @Test
    fun getWorkOfArtByIdWithOnlyRequiredFields() {

        // Given
        val workOfArtId = BsonObjectId()
        val workOfArtUserId = BsonObjectId()

        val workOfArt = WorkOfArt(
            id = workOfArtId,
            user = workOfArtUserId,
            title = "test-title",
            imageUrl = "test-image-url",
            medium = Medium.GOUACHE,
        )

        every { workOfArtRepo.findByIdOrNull(workOfArtId.value.toString()) } returns workOfArt

        // When
        val result = mockMvc.perform(get("/api/woa/${workOfArtId.value}"))

        // Then
        result.andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(workOfArtId.value.toString()))
            .andExpect(jsonPath("$.user").value(workOfArtUserId.value.toString()))
            .andExpect(jsonPath("$.challengeId").doesNotExist())
            .andExpect(jsonPath("$.title").value("test-title"))
            .andExpect(jsonPath("$.description").doesNotExist())
            .andExpect(jsonPath("$.imageUrl").value("test-image-url"))
            .andExpect(jsonPath("$.medium").value("gouache"))
            .andExpect(jsonPath("$.materials").isEmpty())
    }
}
