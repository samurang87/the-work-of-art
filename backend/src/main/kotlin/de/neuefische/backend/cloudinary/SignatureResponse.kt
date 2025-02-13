package de.neuefische.backend.cloudinary

data class SignatureResponse(
    val signature: String,
    val timestamp: String,
    val apiKey: String,
    val cloudName: String,
    val uploadPreset: String,
)
