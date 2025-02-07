package de.neuefische.backend.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleGenericException(exception: Exception): ErrorMessage =
        ErrorMessage(
            status = HttpStatus.INTERNAL_SERVER_ERROR,
            message = exception.message ?: "An error occurred",
        )

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadRequestException(exception: IllegalArgumentException): ErrorMessage =
        ErrorMessage(
            status = HttpStatus.BAD_REQUEST,
            message = exception.message ?: "Bad request",
        )

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFoundException(exception: NoSuchElementException): ErrorMessage =
        ErrorMessage(
            status = HttpStatus.NOT_FOUND,
            message = exception.message ?: "Not found",
        )

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun handleAccessDeniedException(exception: AccessDeniedException): ErrorMessage =
        ErrorMessage(
            status = HttpStatus.FORBIDDEN,
            message = exception.message ?: "Access denied",
        )

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleUnauthorizedException(exception: UnauthorizedException): ErrorMessage =
        ErrorMessage(
            status = HttpStatus.UNAUTHORIZED,
            message = exception.message ?: "Unauthorized",
        )
}
