package de.neuefische.backend.exceptions

import org.springframework.http.HttpStatus

data class ErrorMessage(
    val status: HttpStatus,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
)
