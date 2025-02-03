package de.neuefische.backend.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleGenericException(exception: Exception): ErrorMessage {
        return ErrorMessage(
            status = HttpStatus.INTERNAL_SERVER_ERROR,
            message = exception.message ?: "An error occurred"
        )
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadRequestException(exception: IllegalArgumentException): ErrorMessage {
        return ErrorMessage(
            status = HttpStatus.BAD_REQUEST,
            message = exception.message ?: "Bad request"
        )
    }
}
