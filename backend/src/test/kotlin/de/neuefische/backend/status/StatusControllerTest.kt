package de.neuefische.backend.status

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class StatusControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun getStatus_shouldReturnOk() {
        mockMvc.perform(get("/api/status"))
            .andExpect(status().isOk)
            .andExpect { assertTrue(it.response.contentAsString.isNotEmpty()) }
    }
}
