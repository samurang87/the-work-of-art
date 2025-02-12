package de.neuefische.backend.cloudinary

import com.cloudinary.Cloudinary
import com.cloudinary.util.apiSignRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/api/cloudinary")
class CloudinaryController(
    private val cloudinary: Cloudinary,
) {
    @GetMapping("/signature")
    fun generateSignature(): SignatureResponse {
        val timestamp = Instant.now().epochSecond.toString()
        val params: MutableMap<String, Any> =
            mutableMapOf(
                "source" to "uw",
                "timestamp" to timestamp,
                "upload_preset" to "woa-standard",
            )

        val signature =
            apiSignRequest(
                params,
                requireNotNull(cloudinary.config.apiSecret) { "API Secret is required" },
            )

        val apiKey = requireNotNull(cloudinary.config.apiKey) { "API Key is required" }
        val cloudName = cloudinary.config.cloudName

        return SignatureResponse(
            signature = signature,
            timestamp = timestamp,
            apiKey = apiKey,
            cloudName = cloudName,
            uploadPreset = "woa-standard",
        )
    }
}
