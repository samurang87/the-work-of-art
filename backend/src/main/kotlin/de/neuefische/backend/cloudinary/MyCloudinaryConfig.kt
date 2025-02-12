package de.neuefische.backend.cloudinary

import com.cloudinary.Cloudinary
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MyCloudinaryConfig(
    @Value("\${cloudinary.cloud-name}")
    private val cloudName: String,
    @Value("\${cloudinary.api-key}")
    private val apiKey: String,
    @Value("\${cloudinary.api-secret}")
    private val apiSecret: String,
) {
    @Bean
    fun cloudinary(): Cloudinary = Cloudinary("cloudinary://$apiKey:$apiSecret@$cloudName")
}
